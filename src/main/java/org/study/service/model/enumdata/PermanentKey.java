package org.study.service.model.enumdata;

import lombok.Getter;

/**
 * redis中持久存储的数据类型
 * @author fanqie
 * @date 2020/3/2
 */
@Getter
public enum PermanentKey {

    /* 商品库存 */
    STOCK("stock"),
    /* 商品销量 */
    SALES("sales"),
    /* 购物车数据 */
    CART("cart"),
    /* 售罄标志 */
    SOLD_OUT_MARK("out"),
    ;

    private final String prefix;

    PermanentKey(final String prefix) {
        this.prefix = prefix;
    }
}
