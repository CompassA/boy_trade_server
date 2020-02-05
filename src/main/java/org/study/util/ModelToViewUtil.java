package org.study.util;

import org.springframework.util.CollectionUtils;
import org.study.service.model.OrderDetailModel;
import org.study.service.model.OrderModel;
import org.study.service.model.ProductModel;
import org.study.service.model.UserModel;
import org.study.view.OrderDetailVO;
import org.study.view.OrderVO;
import org.study.view.ProductVO;
import org.study.view.UserVO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fanqie
 * @date 2020/1/4
 */
public final class ModelToViewUtil {

    private ModelToViewUtil() {
    }

    public static Optional<UserVO> getUserVO(final UserModel userModel) {
        if (Objects.isNull(userModel)) {
            return Optional.empty();
        }
        return Optional.of(new UserVO()
                .setAccount(userModel.getAccount())
                .setName(userModel.getName())
                .setUserId(userModel.getUserId())
                .setIconUrl(userModel.getIconUrl()));
    }

    public static Optional<ProductVO> getProductVO(final ProductModel product) {
        if (Objects.isNull(product)) {
            return Optional.empty();
        }
        return Optional.of(new ProductVO()
                .setUserId(product.getUserId())
                .setProductId(product.getProductId())
                .setProductName(product.getProductName())
                .setCategoryId(product.getCategoryId())
                .setDescription(product.getDescription())
                .setIconUrl(product.getIconUrl())
                .setPayStatus(product.getPayStatus())
                .setPrice(product.getPrice())
                .setSales(product.getSales())
                .setStock(product.getStock())
                .setCreateTime(TimeUtil.toString(product.getCreateTime()))
                .setUpdateTime(TimeUtil.toString(product.getUpdateTime())));
    }

    public static Optional<List<ProductVO>> getProductViews(final List<ProductModel> products) {
        final List<ProductVO> views = products.stream()
                .map(ModelToViewUtil::getProductVO)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(views)) {
            return Optional.empty();
        }
        return Optional.of(views);
    }

    public static Optional<OrderVO> getOrderVO(final OrderModel orderModel) {
        if (orderModel == null || orderModel.getOrderId() == null
                || orderModel.getUserId() == null
                || CollectionUtils.isEmpty(orderModel.getProductDetails())) {
            return Optional.empty();
        }
        final List<OrderDetailVO> orderDetails = orderModel.getProductDetails().stream()
                .map(ModelToViewUtil::getOrderDetailVO)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return Optional.of(new OrderVO()
                .setSellerId(orderModel.getSellerId())
                .setOrderId(orderModel.getOrderId())
                .setOrderStatus(orderModel.getOrderStatus())
                .setPayStatus(orderModel.getPayStatus())
                .setOrderAmount(orderModel.getOrderAmount())
                .setUserAddress(orderModel.getUserAddress())
                .setUserId(orderModel.getUserId())
                .setUserName(orderModel.getUserName())
                .setUserPhone(orderModel.getUserPhone())
                .setOrderDetails(orderDetails)
                .setCreateTime(TimeUtil.toString(orderModel.getCreateTime())));
    }

    public static Optional<OrderDetailVO> getOrderDetailVO(final OrderDetailModel detailModel) {
        if (detailModel == null) {
            return Optional.empty();
        }
        return Optional.of(new OrderDetailVO()
                .setOrderId(detailModel.getOrderId())
                .setProductPrice(detailModel.getProductPrice())
                .setProductAmount(detailModel.getProductAmount())
                .setProductName(detailModel.getProductName())
                .setProductId(detailModel.getProductId())
                .setIconUrl(detailModel.getProductIcon())
                .setDetailId(detailModel.getDetailId()));
    }
}
