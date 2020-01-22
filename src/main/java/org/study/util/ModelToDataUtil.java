package org.study.util;

import org.study.data.*;
import org.study.model.ProductModel;
import org.study.model.UserModel;

import java.util.Objects;
import java.util.Optional;

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
}