package org.study.util;

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;
import org.study.data.ProductDO;
import org.study.data.UserDO;
import org.study.service.model.*;
import org.study.view.*;

import java.util.*;
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

    public static List<CartVO> getCartVOList(final CartModel cartModel) {
        if (cartModel == null) {
            return Collections.emptyList();
        }
        final Map<Integer, UserDO> sellerInfoMap = cartModel.getSellerInfoMap();
        final List<CartVO> cartViews = Lists.newArrayList();
        cartModel.getProductsMap().asMap().forEach((sellerId, cartDetailModels) -> {
            //卖家id、卖家姓名
            final CartVO cartVO = new CartVO()
                    .setSellerId(sellerId)
                    .setSellerName(sellerInfoMap.getOrDefault(sellerId, new UserDO()).getName());

            //商品转化
            final List<Integer> productIds = Lists.newArrayList();
            final List<CartDetailVO> details = cartDetailModels.stream().map(cartDetailModel -> {
                final ProductDO productDO = cartDetailModel.getProductDO();
                if (productDO == null) {
                    return null;
                }
                productIds.add(productDO.getId());
                return new CartDetailVO().setProductId(productDO.getId())
                        .setProductName(productDO.getName())
                        .setDescription(productDO.getDescription())
                        .setIconUrl(productDO.getIconUrl())
                        .setPrice(productDO.getPrice())
                        .setNum(cartDetailModel.getNumInCart());
            }).filter(Objects::nonNull).collect(Collectors.toList());

            cartVO.setCartDetails(details);
            cartVO.setSelectedIds(productIds);
            cartViews.add(cartVO);
        });

        return cartViews;
    }
}
