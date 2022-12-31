package org.study.service.model.enumdata;

import lombok.Getter;

/**
 * @author fanqie
 * Created on 2020/2/6
 */
@Getter
public enum CacheType {
    /* 商品详情缓存 */
    PRODUCT("product"),
    /* 商品校验缓存 */
    PRODUCT_VALIDATION("product_validation"),
    /* 用户信息缓存 */
    USER_INFO("user"),
    /* 主页商品 */
    HOME_PAGE("h"),
    ;

    private final String prefix;

    CacheType(final String prefix) {
        this.prefix = prefix;
    }
}
