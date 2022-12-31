package org.study.util;

import org.springframework.util.CollectionUtils;
import org.study.dao.OrderDetailMapper;
import org.study.dao.ProductSaleMapper;
import org.study.dao.ProductStockMapper;
import org.study.data.AddressInfoDO;
import org.study.data.OrderDetailDO;
import org.study.data.OrderMasterDO;
import org.study.data.ProductDO;
import org.study.data.ProductSaleDO;
import org.study.data.ProductStockDO;
import org.study.data.UserDO;
import org.study.data.UserPasswordDO;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.model.AddressInfoModel;
import org.study.service.model.OrderDetailModel;
import org.study.service.model.OrderModel;
import org.study.service.model.ProductModel;
import org.study.service.model.UserModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fanqie
 * Created on 2020/1/4
 */
public final class DataToModelUtil {

    private DataToModelUtil() {
    }

    public static Optional<UserModel> getUserModel(
            final UserDO userDO, final UserPasswordDO userPasswordDO) {
        if (Objects.isNull(userDO) || Objects.isNull(userPasswordDO)) {
            return Optional.empty();
        }
        return Optional.of(new UserModel()
                .setUserId(userDO.getUserId())
                .setAccount(userDO.getAccount())
                .setName(userDO.getName())
                .setIconUrl(userDO.getIconUrl())
                .setCreateTime(userDO.getCreateTime())
                .setUpdateTime(userDO.getUpdateTime())
                .setPassword(userPasswordDO.getPassword()));
    }

    public static Optional<ProductModel> getProductModel(
            final ProductDO product, final ProductStockDO stock, final ProductSaleDO sale) {
        if (Objects.isNull(product) || Objects.isNull(stock)
                || Objects.isNull(sale)) {
            return Optional.empty();
        }
        final Integer productId = product.getId();
        final Integer stockLinkedId = stock.getProductId();
        final Integer saleLinkedId = sale.getProductId();
        if (Objects.isNull(productId) || !productId.equals(saleLinkedId)
                || !productId.equals(stockLinkedId)) {
            return Optional.empty();
        }
        return Optional.of(new ProductModel().setProductId(productId)
                .setUserId(product.getUserId())
                .setProductName(product.getName())
                .setCategoryId(product.getCategoryId())
                .setDescription(product.getDescription())
                .setIconUrl(product.getIconUrl())
                .setPayStatus(product.getStatus())
                .setPrice(product.getPrice())
                .setSales(sale.getSales())
                .setStock(stock.getStock())
                .setCreateTime(product.getCreateTime())
                .setUpdateTime(product.getUpdateTime()));
    }

    public static Optional<List<ProductModel>> getProductModels(
            final List<ProductDO> products,
            final Map<Integer, ProductStockDO> stockMap,
            final Map<Integer, ProductSaleDO> saleMap) {
        final List<ProductModel> models = products.stream()
                .map(product -> DataToModelUtil.getProductModel(
                        product, stockMap.get(product.getId()), saleMap.get(product.getId())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(models)) {
            return Optional.empty();
        }
        return Optional.of(models);
    }

    public static Optional<List<ProductModel>> getProductModels(final List<ProductDO> products,
            final List<ProductStockDO> stocks, final List<ProductSaleDO> sales) {
        if (CollectionUtils.isEmpty(products) || CollectionUtils.isEmpty(stocks)
                || CollectionUtils.isEmpty(sales)) {
            return Optional.empty();
        }

        //查询商品销量, 库存
        final Map<Integer, ProductStockDO> stockMap = DataToModelUtil.getStockMap(stocks);
        final Map<Integer, ProductSaleDO> saleMap = DataToModelUtil.getSaleMap(sales);

        //组装成商品领域模型
        return DataToModelUtil.getProductModels(products, stockMap, saleMap);
    }

    public static Optional<OrderModel> getOrderModel(
            final OrderMasterDO master, final List<OrderDetailDO> details) {
        if (master == null || CollectionUtils.isEmpty(details)) {
            return Optional.empty();
        }
        return Optional.of(new OrderModel()
                .setSellerId(master.getSellerId())
                .setUpdateTime(master.getUpdateTime())
                .setOrderAmount(master.getOrderAmount())
                .setCreateTime(master.getCreateTime())
                .setProductDetails(DataToModelUtil.getDetailModels(details))
                .setOrderStatus(master.getOrderStatus())
                .setPayStatus(master.getPayStatus())
                .setUserAddress(master.getUserAddress())
                .setUserPhone(master.getUserPhone())
                .setUserName(master.getUserName())
                .setUserId(master.getUserId())
                .setOrderId(master.getOrderId()));
    }

    public static List<OrderDetailModel> getDetailModels(final List<OrderDetailDO> details) {
        return details.stream()
                .map(detailDO -> new OrderDetailModel()
                        .setOwnerId(detailDO.getOwnerId())
                        .setDetailId(detailDO.getDetailId())
                        .setOrderId(detailDO.getOrderId())
                        .setProductIcon(detailDO.getProductIcon())
                        .setProductAmount(detailDO.getProductAmount())
                        .setProductPrice(detailDO.getProductPrice())
                        .setProductName(detailDO.getProductName())
                        .setProductId(detailDO.getProductId())
                        .setCreateTime(detailDO.getCreateTime())
                        .setUpdateTime(detailDO.getUpdateTime())
                ).collect(Collectors.toList());
    }

    public static AddressInfoModel getAddressInfoModel(
            final Integer userId, final List<AddressInfoDO> info) {
        return new AddressInfoModel()
                .setUserId(userId)
                .setUserAddressInfo(
                        CollectionUtils.isEmpty(info) ? new ArrayList<>(0) : info);
    }

    public static List<Integer> getProductId(final List<ProductDO> productCollection) {
        return productCollection.stream().map(ProductDO::getId).collect(Collectors.toList());
    }

    public static Map<Integer, ProductSaleDO> getSaleMap(final List<ProductSaleDO> sales) {
        return sales.stream().collect(Collectors.toMap(
                ProductSaleDO::getProductId, item -> item, (oldVal, newVal) -> newVal));
    }

    public static Map<Integer, ProductStockDO> getStockMap(final List<ProductStockDO> stocks) {
        return stocks.stream().collect(Collectors.toMap(
                ProductStockDO::getProductId, item -> item, (oldVal, newVal) -> newVal));
    }

    public static List<OrderModel> getOrderModel(final List<OrderMasterDO> orderMasters,
            final OrderDetailMapper orderDetailMapper) throws ServerException {
        final List<OrderModel> models = orderMasters.stream()
                .map(orderMasterDO -> DataToModelUtil.getOrderModel(
                        orderMasterDO,
                        orderDetailMapper.selectDetailByOrderId(orderMasterDO.getOrderId())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (models.size() == orderMasters.size()) {
            return models;
        }
        throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_SYSTEM_EXCEPTION);
    }

    /** 没有拼装销量、库存、付款数 */
    public static List<ProductModel> getProductPageModels(
            List<ProductDO> products) {
        if (CollectionUtils.isEmpty(products)) {
            return Collections.emptyList();
        }
        return products.stream().map(productDO -> new ProductModel().setProductId(productDO.getId())
                .setUserId(productDO.getUserId())
                .setProductName(productDO.getName())
                .setCategoryId(productDO.getCategoryId())
                .setDescription(productDO.getDescription())
                .setIconUrl(productDO.getIconUrl())
                .setPayStatus(productDO.getStatus())
                .setPrice(productDO.getPrice())
                .setCreateTime(productDO.getCreateTime())
                .setUpdateTime(productDO.getUpdateTime())
        ).collect(Collectors.toList());
    }

    public static List<ProductModel> getProductModels(List<ProductDO> products,
            ProductStockMapper stockMapper, ProductSaleMapper saleMapper) throws ServerException {
        if (CollectionUtils.isEmpty(products)) {
            return Collections.emptyList();
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
