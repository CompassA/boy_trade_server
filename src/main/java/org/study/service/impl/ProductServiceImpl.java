package org.study.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.ProductMapper;
import org.study.dao.ProductSaleMapper;
import org.study.dao.ProductStockMapper;
import org.study.data.ProductDO;
import org.study.data.ProductSaleDO;
import org.study.data.ProductStockDO;
import org.study.error.ServerException;
import org.study.error.ServerExceptionEnum;
import org.study.mq.Producer;
import org.study.service.ProductService;
import org.study.service.RedisService;
import org.study.service.model.ProductModel;
import org.study.service.model.enumdata.CacheType;
import org.study.service.model.enumdata.PermanentKey;
import org.study.service.model.enumdata.ProductStatus;
import org.study.util.DataToModelUtil;
import org.study.util.ModelToDataUtil;
import org.study.util.MyMathUtil;
import org.study.util.MyStringUtil;
import org.study.validation.ValidationResult;
import org.study.validation.ValidatorImpl;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author fanqie
 * Created on 2020/1/12
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductSaleMapper saleMapper;

    @Resource
    private ProductStockMapper stockMapper;

    @Resource
    private ValidatorImpl validator;

    @Resource
    private RedisService redisService;

    @Resource
    private Producer producer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductModel create(final ProductModel productModel) throws ServerException {
        //校验
        final ValidationResult result = validator.validate(productModel);
        if (result.hasErrors() || MyMathUtil.isZeroOrNegative(productModel.getPrice())) {
            throw new ServerException(ServerExceptionEnum.PRODUCT_CREATE_EXCEPTION);
        }

        //ProductModel拆分为ProductDO、StockDO、SalesDO
        final Optional<ProductDO> productDO = ModelToDataUtil.getProductDO(productModel);
        final Optional<ProductStockDO> stockDO = ModelToDataUtil.getStockDO(productModel);
        if (!productDO.isPresent() || !stockDO.isPresent()) {
            throw new ServerException(ServerExceptionEnum.PRODUCT_CREATE_EXCEPTION);
        }

        //product入库并返回主键
        final ProductDO product = productDO.get();
        productMapper.upsertProduct(product);
        final Integer productId = product.getId();

        //库存入库并同步redis
        final ProductStockDO stock = stockDO.get().setProductId(productId);
        stockMapper.initProductStock(stock);
        redisService.saveWithoutExpire(
                MyStringUtil.getPermanentKey(productId, PermanentKey.STOCK),
                stock.getStock());

        //销量入库并同步redis
        final ProductSaleDO sale = new ProductSaleDO().setProductId(productId);
        saleMapper.initProductSale(sale);
        redisService.saveWithoutExpire(
                MyStringUtil.getPermanentKey(productId, PermanentKey.SALES), 0);

        //返回数据库状态
        final Optional<ProductModel> productStatus = DataToModelUtil.getProductModel(
                productMapper.selectByPrimaryKey(productId),
                stockMapper.selectStock(productId),
                saleMapper.selectSale(productId));
        if (productStatus.isPresent()) {
            return productStatus.get();
        }
        throw new ServerException(ServerExceptionEnum.PRODUCT_CREATE_EXCEPTION);
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
        throw new ServerException(ServerExceptionEnum.PRODUCT_NOT_EXIST_EXCEPTION);
    }

    @Override
    public ProductModel selectWithoutStockAndSales(int productId) throws ServerException {
        ProductDO productInfo = null;

        final String key = MyStringUtil.getCacheKey(productId, CacheType.PRODUCT_VALIDATION);
        final Optional<ProductDO> cache = redisService.getCache(key);
        if (cache.isPresent()) {
            productInfo = cache.get();
        } else {
            productInfo = productMapper.selectByPrimaryKey(productId);
            if (productInfo == null) {
                throw new ServerException(ServerExceptionEnum.PRODUCT_NOT_EXIST_EXCEPTION);
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
    public List<ProductModel> selectByUserId(final int userId) throws ServerException {
        final ProductDO condition = new ProductDO().setUserId(userId);
        final List<ProductDO> products = productMapper.selectProduct(condition);
        return DataToModelUtil.getProductModels(products, stockMapper, saleMapper);
    }

    @Override
    public List<ProductModel> selectTopFive(final Integer typeId) {
        if (typeId == null) {
            return Collections.emptyList();
        }
        final List<ProductDO> productData = productMapper.selectTopFive(typeId);
        return DataToModelUtil.getProductPageModels(productData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseStock(final Integer productId, final Integer amount) {
        //减redis库存
        final String key = MyStringUtil.getPermanentKey(productId, PermanentKey.STOCK);
        final Long resNum = redisService.decreaseKey(key, amount);

        //扣减合法性判断
        if (resNum == null || resNum < 0) {
            redisService.increaseKey(key, amount);
            return false;
        }

        //发消息，减mysql库存
        final boolean decreased = producer.asyncDecreaseStock(productId, amount);
        if (decreased) {
            redisService.deleteKey(MyStringUtil.getCacheKey(productId, CacheType.PRODUCT));
        } else {
            redisService.increaseKey(key, amount);
        }
        return decreased;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseStock(final Integer productId, final Integer amount) {
        return stockMapper.increaseStock(productId, amount) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseSales(final Integer productId, final Integer amount) {
        //加redis销量
        final String key = MyStringUtil.getPermanentKey(productId, PermanentKey.SALES);
        redisService.increaseKey(key, amount);

        //发消息增加mysql销量, 发送消息失败则回滚数据
        final boolean increased = producer.asyncIncreaseSale(productId, amount);
        if (increased) {
            redisService.deleteKey(MyStringUtil.getCacheKey(productId, CacheType.PRODUCT));
        } else {
            redisService.decreaseKey(key, amount);
        }
        return increased;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseStockIncreaseSales(final Integer productId, final Integer amount) {
        //decrease stock in redis
        final String stockKey = MyStringUtil.getPermanentKey(productId, PermanentKey.STOCK);
        final Long resNum = redisService.decreaseKey(stockKey, amount);

        //check rest number
        if (resNum == null || resNum < 0) {
            redisService.increaseKey(stockKey, amount);
            return false;
        }

        //increase sales in redis
        final String salesKey = MyStringUtil.getPermanentKey(productId, PermanentKey.SALES);
        redisService.increaseKey(salesKey, amount);

        //set sold out mark
        if (resNum == 0) {
            this.withdrawFromShelves(productId);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseStockDecreaseSales(final Integer productId, final Integer amount) {
        //decrease sales in redis
        final String salesKey = MyStringUtil.getPermanentKey(productId, PermanentKey.SALES);
        final Long resNum = redisService.decreaseKey(salesKey, amount);

        //check rest num
        if (resNum == null || resNum < 0) {
            redisService.increaseKey(salesKey, amount);
            return false;
        }

        //increase stock
        final String stockKey = MyStringUtil.getPermanentKey(productId, PermanentKey.STOCK);
        redisService.increaseKey(stockKey, amount);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseSales(final Integer productId, final Integer amount) {
        return saleMapper.decreaseSales(productId, amount) > 0;
    }

    @Override
    public BigDecimal getProductPrice(final ProductModel productModel) {
        return productModel.getPrice();
    }

    @Override
    public List<ProductDO> getProductInfoByIds(final List<Integer> productIds) {
        return productMapper.selectInKeyList(productIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdrawFromShelves(final Integer productId) {
        //set sold out mark
        String key = MyStringUtil.getPermanentKey(productId, PermanentKey.SOLD_OUT_MARK);
        redisService.saveWithoutExpire(key, -1);

        //change mysql
        final ProductDO condition = new ProductDO().setId(productId)
                .setStatus(ProductStatus.SOLD_OUT.getValue());
        productMapper.upsertProduct(condition);

        //expire validate cache
        redisService.deleteKey(MyStringUtil.getCacheKey(productId, CacheType.PRODUCT_VALIDATION));
    }

    @Override
    public void removeProduct(final Integer productId) {
        //change mysql
        final ProductDO condition = new ProductDO().setId(productId)
                .setStatus(ProductStatus.REMOVED.getValue());
        productMapper.upsertProduct(condition);

        //delete stock in redis
        redisService.deleteKey(MyStringUtil.getPermanentKey(productId, PermanentKey.STOCK));
        redisService.deleteKey(MyStringUtil.getPermanentKey(productId, PermanentKey.SALES));
        redisService.deleteKey(MyStringUtil.getPermanentKey(productId, PermanentKey.PAID_NUM));

        //delete cache
        redisService.deleteKey(MyStringUtil.getCacheKey(productId, CacheType.PRODUCT_VALIDATION));
        redisService.deleteKey(MyStringUtil.getCacheKey(productId, CacheType.PRODUCT));

        //delete sell out mark in redis
        redisService.deleteKey(MyStringUtil.getPermanentKey(productId, PermanentKey.SOLD_OUT_MARK));
    }

    @Override
    public boolean reInSale(final Integer productId) {
        final ProductDO condition = new ProductDO().setId(productId)
                .setStatus(ProductStatus.IN_SALE.getValue());
        redisService.deleteKey(MyStringUtil.getPermanentKey(productId, PermanentKey.SOLD_OUT_MARK));
        return productMapper.upsertProduct(condition) > 0;
    }

    @Override
    public List<ProductModel> selectFromBegin(Integer pageNum) {
        final List<ProductDO> products = productMapper.selectFromBegin(pageNum * PAGE_SIZE);
        return DataToModelUtil.getProductPageModels(products);
    }

    @Override
    public List<ProductModel> selectNextPage(Integer preLastId, Integer prePage,
                                             Integer targetPage, Integer typeId) {
        final int gap = (targetPage - prePage - 1) * PAGE_SIZE;
        if (gap < 0) {
            return Collections.emptyList();
        }
        List<ProductDO> products = productMapper.selectNextPage(preLastId, gap, PAGE_SIZE, typeId);
        return DataToModelUtil.getProductPageModels(products);
    }

    @Override
    public List<ProductModel> selectPageNormal(Integer targetPage, Integer typeId) {
        final int gap = (targetPage - 1) * PAGE_SIZE;
        final List<ProductDO> productDO = productMapper.selectPageNormal(gap, PAGE_SIZE, typeId);
        return DataToModelUtil.getProductPageModels(productDO);
    }

    @Override
    public boolean isSoldOut(final Integer id) {
        final String key = MyStringUtil.getPermanentKey(id, PermanentKey.SOLD_OUT_MARK);
        return redisService.getPermanentStr(key).isPresent();
    }

    @Override
    public Integer getPaidNum(Integer id) {
        final String key = MyStringUtil.getPermanentKey(id, PermanentKey.PAID_NUM);
        return redisService.getPermanentInt(key).orElse(0);
    }

    @Override
    public void increasePaidNum(Integer id, Integer amount) {
        final String key = MyStringUtil.getPermanentKey(id, PermanentKey.PAID_NUM);
        redisService.increaseKey(key, amount == null ? 0 : amount);
    }

    @Override
    public boolean isProductAllPaid(Integer id) {
        final String stockKey = MyStringUtil.getPermanentKey(id, PermanentKey.STOCK);
        final String salesKey = MyStringUtil.getPermanentKey(id, PermanentKey.SALES);
        final int stock = redisService.getPermanentInt(stockKey).orElse(0);
        final int sales = redisService.getPermanentInt(salesKey).orElse(0);
        final int paidNum = this.getPaidNum(id);
        return stock == 0 && paidNum == sales;
    }

    @Override
    public void delDetailCache(Integer id) {
        redisService.deleteKey(MyStringUtil.getCacheKey(id, CacheType.PRODUCT));
    }
}
