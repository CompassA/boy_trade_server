package org.study.util;

import org.study.model.ProductModel;
import org.study.model.UserModel;
import org.study.view.ProductVO;
import org.study.view.UserVO;

import java.util.Objects;
import java.util.Optional;

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

    public static Optional<ProductVO> getProductVO(
            final ProductModel product) {
        if (Objects.isNull(product)) {
            return Optional.empty();
        }
        return Optional.of(new ProductVO()
                .setProductId(product.getProductId())
                .setProductName(product.getProductName())
                .setCategoryId(product.getCategoryId())
                .setDescription(product.getDescription())
                .setIconUrl(product.getIconUrl())
                .setPayStatus(product.getPayStatus())
                .setPrice(product.getPrice())
                .setSales(product.getSales())
                .setStock(product.getStock()));
    }
}
