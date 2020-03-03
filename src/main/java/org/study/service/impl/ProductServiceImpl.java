package org.study.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.study.dao.ProductMapper;
import org.study.dao.ProductSaleMapper;
import org.study.dao.ProductStockMapper;
import org.study.data.ProductDO;
import org.study.data.ProductSaleDO;
import org.study.data.ProductStockDO;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.mq.Producer;
import org.study.service.ProductService;
import org.study.service.RedisService;
import org.study.service.model.ProductModel;
import org.study.service.model.enumdata.CacheType;
import org.study.service.model.enumdata.PermanentValueType;
import org.study.util.DataToModelUtil;
import org.study.util.ModelToDataUtil;
import org.study.util.MyMathUtil;
import org.study.util.MyStringUtil;
import org.study.validation.ValidationResult;
import org.study.validation.ValidatorImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fanqie
 * @date 2020/1/12
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSaleMapper saleMapper;

    @Autowired
    private ProductStockMapper stockMapper;

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private RedisService redisService;

    @Autowired
    private Producer producer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductModel create(final ProductModel productModel) throws ServerException {
        //校验
        final ValidationResult result = validator.validate(productModel);
        if (result.hasErrors() || MyMathUtil.isZeroOrNegative(productModel.getPrice())) {
            throw new ServerException(ServerExceptionBean.PRODUCT_CREATE_EXCEPTION);
        }

        //ProductModel拆分为ProductDO、StockDO、SalesDO
        final Optional<ProductDO> productDO = ModelToDataUtil.getProductDO(productModel);
        final Optional<ProductStockDO> stockDO = ModelToDataUtil.getStockDO(productModel);
        if (!productDO.isPresent() || !stockDO.isPresent()) {
            throw new ServerException(ServerExceptionBean.PRODUCT_CREATE_EXCEPTION);
        }

        //product入库并返回主键
        final ProductDO product = productDO.get();
        productMapper.upsertProduct(product);
        final Integer productId = product.getId();

        //库存入库并同步redis
        final ProductStockDO stock = stockDO.get().setProductId(productId);
        stockMapper.initProductStock(stock);
        redisService.saveWithoutExpire(
                MyStringUtil.generatePermanentKey(productId, PermanentValueType.STOCK),
                stock.getStock());

        //销量入库并同步redis
        final ProductSaleDO sale = new ProductSaleDO().setProductId(productId);
        saleMapper.initProductSale(sale);
        redisService.saveWithoutExpire(
                MyStringUtil.generatePermanentKey(productId, PermanentValueType.SALES), 0);

        //返回数据库状态
        final Optional<ProductModel> productStatus = DataToModelUtil.getProductModel(
                productMapper.selectByPrimaryKey(productId),
                stockMapper.selectStock(productId),
                saleMapper.selectSale(productId));
        if (productStatus.isPresent()) {
            return productStatus.get();
        }
        throw new ServerException(ServerExceptionBean.PRODUCT_CREATE_EXCEPTION);
    }

    @Override
    public ProductModel selectByPrimaryKey(final int productId) throws ServerException {
        final Optional<ProductModel> model = DataToModelUtil.getProductModel(
                productMapper.selectByPrimaryKey(productId),
                stockMapper.selectStock(productId),
                saleMapper.selectSale(productId));
        if (model.isPresent()) {
            return model.get();
        }
        throw new ServerException(ServerExceptionBean.PRODUCT_NOT_EXIST_EXCEPTION);
    }

    @Override
    public ProductModel selectWithoutStockAndSales(int productId) throws ServerException {
        ProductDO productInfo = null;

        final String key = MyStringUtil.generateCacheKey(productId, CacheType.PRODUCT_VALIDATION);
        final Optional<ProductDO> cache = redisService.getCache(key, ProductDO.class);
        if (cache.isPresent()) {
            productInfo = cache.get();
        } else {
            productInfo = productMapper.selectByPrimaryKey(productId);
            if (productInfo == null) {
                throw new ServerException(ServerExceptionBean.PRODUCT_NOT_EXIST_EXCEPTION);
            }
            redisService.cacheData(key, productInfo);
        }

        return new ProductModel().setProductId(productId)
                .setUserId(productInfo.getUserId())
                .setProductName(productInfo.getName())
                .setCategoryId(productInfo.getCategoryId())
                .setDescription(productInfo.getDescription())
                .setIconUrl(productInfo.getIconUrl())
                .setPayStatus(productInfo.getStatus())
                .setPrice(productInfo.getPrice())
                .setCreateTime(productInfo.getCreateTime())
                .setUpdateTime(productInfo.getUpdateTime());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseStock(final Integer productId, final Integer amount) {
        //减redis库存
        final String key = MyStringUtil.generatePermanentKey(productId, PermanentValueType.STOCK);
        final Long resNum = redisService.decreaseKey(key, amount);

        //扣减合法性判断
        if (resNum == null || resNum < 0) {
            redisService.increaseKey(key, amount);
            return false;
        }

        //发消息，减mysql库存
        final boolean decreased = producer.asyncDecreaseStock(productId, amount);
        if (decreased) {
            redisService.deleteCache(MyStringUtil.generateCacheKey(productId, CacheType.PRODUCT));
        } else {
            redisService.increaseKey(key, amount);
        }
        return decreased;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseStock(final Integer productId, final Integer amount) {
        //加redis库存
        final String key = MyStringUtil.generatePermanentKey(productId, PermanentValueType.STOCK);
        redisService.increaseKey(key, amount);

        //发消息加mysql库存
        final boolean increased = producer.asyncIncreaseStock(productId, amount);
        if (increased) {
            redisService.deleteCache(MyStringUtil.generateCacheKey(productId, CacheType.PRODUCT));
        } else {
            redisService.decreaseKey(key, amount);
        }
        return increased;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseSales(final Integer productId, final Integer amount) {
        //加redis销量
        final String key = MyStringUtil.generatePermanentKey(productId, PermanentValueType.SALES);
        redisService.increaseKey(key, amount);

        //发消息增加mysql销量, 发送消息失败则回滚数据
        final boolean increased = producer.asyncIncreaseSale(productId, amount);
        if (increased) {
            redisService.deleteCache(MyStringUtil.generateCacheKey(productId, CacheType.PRODUCT));
        } else {
            redisService.decreaseKey(key, amount);
        }
        return increased;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseStockIncreaseSales(final Integer productId, final Integer amount) {
        //减redis库存
        final String stockKey = MyStringUtil
                .generatePermanentKey(productId, PermanentValueType.STOCK);
        final Long resNum = redisService.decreaseKey(stockKey, amount);

        //扣减合法性判断
        if (resNum == null || resNum < 0) {
            redisService.increaseKey(stockKey, amount);
            return false;
        }

        //增销量
        final String salesKey = MyStringUtil
                .generatePermanentKey(productId, PermanentValueType.SALES);
        redisService.increaseKey(salesKey, amount);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseStockDecreaseSales(final Integer productId, final Integer amount) {
        //减销量
        final String salesKey = MyStringUtil
                .generatePermanentKey(productId, PermanentValueType.SALES);
        final Long resNum = redisService.decreaseKey(salesKey, amount);

        //扣减合法性
        if (resNum == null || resNum < 0) {
            redisService.increaseKey(salesKey, amount);
            return false;
        }

        //增库存
        final String stockKey = MyStringUtil
                .generatePermanentKey(productId, PermanentValueType.STOCK);
        redisService.increaseKey(stockKey, amount);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseSales(final Integer productId, final Integer amount) {
        //减redis销量
        final String key = MyStringUtil.generatePermanentKey(productId, PermanentValueType.SALES);
        final Long resNum = redisService.decreaseKey(key, amount);

        //扣减合法性判断
        if (resNum == null || resNum < 0) {
            redisService.increaseKey(key, amount);
            return false;
        }

        //发消息加库存, 发送消息失败回滚数据
        final boolean decreased = producer.asyncDecreaseSale(productId, amount);
        if (decreased) {
            redisService.deleteCache(MyStringUtil.generateCacheKey(productId, CacheType.PRODUCT));
        } else {
            redisService.increaseKey(key, amount);
        }
        return decreased;
    }

    /** 获取所有商品， 仅测试用 */
    @Deprecated
    @Override
    public List<ProductModel> getAllProduct() throws ServerException {
        return query(new ProductDO());
    }

    private List<ProductModel> query(final ProductDO condition) throws ServerException {
        final List<ProductDO> products = productMapper.selectProduct(condition);
        if (CollectionUtils.isEmpty(products)) {
            throw new ServerException(ServerExceptionBean.PRODUCT_NOT_EXIST_EXCEPTION);
        }

        //获取所有商品id
        final List<Integer> productIds = products.stream()
                .map(ProductDO::getId).collect(Collectors.toList());

        //查询商品销量, 库存
        final List<ProductStockDO> stocks = stockMapper.selectProductStock(productIds);
        final List<ProductSaleDO> sales = saleMapper.selectProductSale(productIds);

        //组装成商品领域模型
        final Optional<List<ProductModel>> models =
                DataToModelUtil.getProductModels(products, stocks, sales);
        if (models.isPresent()) {
            return models.get();
        }

        throw new ServerException(ServerExceptionBean.PRODUCT_NOT_EXIST_EXCEPTION);
    }

    @Override
    public BigDecimal getProductPrice(final ProductModel productModel) {
        return productModel.getPrice();
    }

    @Override
    public List<ProductDO> getProductInfoByIds(final List<Integer> productIds) {
        return productMapper.selectInKeyList(productIds);
    }
}
