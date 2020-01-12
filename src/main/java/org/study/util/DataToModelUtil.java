package org.study.util;

import org.study.data.*;
import org.study.model.ProductModel;
import org.study.model.UserModel;

import java.util.Objects;
import java.util.Optional;

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
            final ProductDO product,
            final ProductStockDO stock,
            final ProductSaleDO sale) {
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
}
