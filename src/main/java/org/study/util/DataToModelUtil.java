package org.study.util;

import org.springframework.util.CollectionUtils;
import org.study.data.*;
import org.study.service.model.OrderDetailModel;
import org.study.service.model.OrderModel;
import org.study.service.model.ProductModel;
import org.study.service.model.UserModel;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fanqie
 * @date 2020/1/4
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

    public static Optional<List<ProductModel>> getProductModels(final List<ProductDO> products,
            final List<ProductStockDO> stocks, final List<ProductSaleDO> sales) {
        if (CollectionUtils.isEmpty(products) || CollectionUtils.isEmpty(stocks)
                || CollectionUtils.isEmpty(sales)) {
            return Optional.empty();
        }

        //查询商品销量, 库存
        final Map<Integer, ProductStockDO> stockMap = stocks.stream().collect(Collectors.toMap(
                        ProductStockDO::getProductId, item -> item, (oldVal, newVal) -> newVal));
        final Map<Integer, ProductSaleDO> saleMap = sales.stream().collect(Collectors.toMap(
                        ProductSaleDO::getProductId, item -> item, (oldVal, newVal) -> newVal));

        //组装成商品领域模型
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
}
