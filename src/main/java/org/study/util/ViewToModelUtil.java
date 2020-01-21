package org.study.util;

import org.study.model.ProductModel;
import org.study.view.ProductVO;

import java.util.Objects;
import java.util.Optional;

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
}
