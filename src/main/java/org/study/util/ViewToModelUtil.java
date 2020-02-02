package org.study.util;

import org.springframework.util.CollectionUtils;
import org.study.service.model.OrderDetailModel;
import org.study.service.model.OrderModel;
import org.study.service.model.ProductModel;
import org.study.view.OrderDTO;
import org.study.view.OrderDetailDTO;
import org.study.view.ProductVO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fanqie
 * @date 2020/1/12
 */
public final class ViewToModelUtil {

    private ViewToModelUtil() {
    }

    public static Optional<ProductModel> getProductModel(final ProductVO productVO) {
        if (Objects.isNull(productVO)) {
            return Optional.empty();
        }
        return Optional.of(new ProductModel()
                .setProductId(productVO.getProductId())
                .setProductName(productVO.getProductName())
                .setCategoryId(productVO.getCategoryId())
                .setDescription(productVO.getDescription())
                .setIconUrl(productVO.getIconUrl())
                .setPayStatus(productVO.getPayStatus())
                .setPrice(productVO.getPrice())
                .setSales(productVO.getSales())
                .setStock(productVO.getStock()));
    }

    public static Optional<OrderModel> getOrderModel(final OrderDTO orderDTO) {
        if (orderDTO == null) {
            return Optional.empty();
        }
        return ViewToModelUtil.getOrderDetails(orderDTO.getProductDetails())
                .map(orderDetailModels -> new OrderModel()
                        .setUserId(orderDTO.getUserId())
                        .setUserName(orderDTO.getUserName())
                        .setUserPhone(orderDTO.getUserPhone())
                        .setUserAddress(orderDTO.getUserAddress())
                        .setProductDetails(orderDetailModels));
    }

    public static Optional<List<OrderDetailModel>> getOrderDetails(
            final List<OrderDetailDTO> details) {
        if (CollectionUtils.isEmpty(details)) {
            return Optional.empty();
        }
        final List<OrderDetailModel> models = details.stream()
                .map(ViewToModelUtil::getOrderDetailModel)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(models)) {
            return Optional.empty();
        }
        return Optional.of(models);
    }

    public static Optional<OrderDetailModel> getOrderDetailModel(
            final OrderDetailDTO dto) {
        if (dto == null) {
            return Optional.empty();
        }
        return Optional.of(new OrderDetailModel()
                .setProductId(dto.getProductId())
                .setProductName(dto.getProductName())
                .setProductPrice(dto.getProductPrice())
                .setProductIcon(dto.getIconUrl())
                .setProductAmount(dto.getProductAmount()));
    }
}
