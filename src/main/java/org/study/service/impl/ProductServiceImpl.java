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
import org.study.model.ProductModel;
import org.study.service.ProductService;
import org.study.util.DataToModelUtil;
import org.study.util.ModelToDataUtil;
import org.study.util.MyMathUtil;
import org.study.validation.ValidationResult;
import org.study.validation.ValidatorImpl;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductModel create(final ProductModel productModel) throws ServerException {
        //校验
        final ValidationResult result = validator.validate(productModel);
        if (result.hasErrors() || MyMathUtil.isZeroOrNegative(productModel.getPrice())) {
            throw new ServerException(ServerExceptionBean.PRODUCT_CREATE_EXCEPTION);
        }

        //入库
        final Optional<ProductDO> productDO = ModelToDataUtil.getProductDO(productModel);
        final Optional<ProductStockDO> stockDO = ModelToDataUtil.getStockDO(productModel);
        if (!productDO.isPresent() || !stockDO.isPresent()) {
            throw new ServerException(ServerExceptionBean.PRODUCT_CREATE_EXCEPTION);
        }
        final ProductDO product = productDO.get();
        productMapper.upsertProduct(product);
        final Integer productId = product.getId();
        final ProductStockDO stock = stockDO.get().setProductId(productId);
        final ProductSaleDO sale = new ProductSaleDO().setProductId(productId);
        stockMapper.initProductStock(stock);
        saleMapper.initProductSale(sale);

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
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseStock(final Integer productId, final Integer amount) {
        return stockMapper.decreaseStock(productId, amount) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseSales(final Integer productId, final Integer amount) {
        return saleMapper.increaseSales(productId, amount) > 0;
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
}
