package org.study.util;

import org.springframework.util.CollectionUtils;
import org.study.data.*;
import org.study.model.OrderDetailModel;
import org.study.model.OrderModel;
import org.study.model.ProductModel;
import org.study.model.UserModel;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fanqie
 * @date 2020/1/12
 */
public final class ModelToDataUtil {

    private ModelToDataUtil() {
    }

    public static Optional<ProductDO> getProductDO(final ProductModel productModel) {
        if (Objects.isNull(productModel)) {
            return Optional.empty();
        }
        return Optional.of(new ProductDO().setId(productModel.getProductId())
                .setUserId(productModel.getUserId())
                .setCategoryId(productModel.getCategoryId())
                .setDescription(productModel.getDescription())
                .setIconUrl(productModel.getIconUrl())
                .setName(productModel.getProductName())
                .setPrice(productModel.getPrice())
                .setStatus(productModel.getPayStatus())
                .setUpdateTime(productModel.getUpdateTime())
                .setCreateTime(productModel.getCreateTime()));
    }

    public static Optional<ProductSaleDO> getSaleDO(final ProductModel productModel) {
        if (Objects.isNull(productModel)) {
            return Optional.empty();
        }
        return Optional.of(new ProductSaleDO()
                .setProductId(productModel.getProductId())
                .setSales(productModel.getSales())
                .setCreateTime(productModel.getCreateTime())
                .setUpdateTime(productModel.getUpdateTime()));
    }

    public static Optional<ProductStockDO> getStockDO(final ProductModel productModel) {
        if (Objects.isNull(productModel)) {
            return Optional.empty();
        }
        return Optional.of(new ProductStockDO()
                .setProductId(productModel.getProductId())
                .setStock(productModel.getStock())
                .setCreateTime(productModel.getCreateTime())
                .setUpdateTime(productModel.getUpdateTime()));
    }

    public static Optional<UserDO> getUserDO(final UserModel userModel) {
        if (Objects.isNull(userModel)) {
            return Optional.empty();
        }
        return Optional.of(new UserDO()
                .setUserId(userModel.getUserId())
                .setAccount(userModel.getAccount())
                .setName(userModel.getName())
                .setIconUrl(userModel.getIconUrl()));
    }

    public static Optional<UserPasswordDO> getUserPasswordDO(final UserModel userModel) {
        if (Objects.isNull(userModel)) {
            return Optional.empty();
        }
        return Optional.of(new UserPasswordDO()
                .setUserId(userModel.getUserId())
                .setPassword(userModel.getPassword()));
    }

    public static Optional<OrderMasterDO> getOrderMaster(final OrderModel orderModel) {
        if (orderModel.getOrderId() == null || orderModel.getUserId() == null) {
            return Optional.empty();
        }
        return Optional.of(new OrderMasterDO()
                .setUserId(orderModel.getUserId())
                .setUserName(orderModel.getUserName())
                //TODO  接入手机号和用户地址
                .setUserPhone("")
                .setUserAddress("")
                .setPayStatus(orderModel.getPayStatus())
                .setCreateTime(orderModel.getCreateTime())
                .setOrderStatus(orderModel.getOrderStatus())
                .setOrderAmount(orderModel.getOrderAmount())
                .setOrderId(orderModel.getOrderId()));
    }

    public static Optional<List<OrderDetailDO>> getOrderDetails(
            final List<OrderDetailModel> details, final String orderId) {
        if (CollectionUtils.isEmpty(details)) {
            return Optional.empty();
        }
        final List<OrderDetailDO> detailList = details.stream()
                .map(detailModel -> ModelToDataUtil.getOrderDetail(detailModel, orderId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(detailList) || detailList.size() != details.size()) {
            return Optional.empty();
        }
        return Optional.of(detailList);
    }

    public static Optional<OrderDetailDO> getOrderDetail(
            final OrderDetailModel model, final String orderId) {
        if (model == null) {
            return Optional.empty();
        }
        model.setOrderId(orderId);
        return Optional.of(new OrderDetailDO()
                .setProductIcon(model.getProductIcon())
                .setProductPrice(model.getProductPrice())
                .setProductAmount(model.getProductAmount())
                .setProductName(model.getProductName())
                .setProductId(model.getProductId())
                .setOrderId(orderId));
    }
}
