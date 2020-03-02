package org.study.service.model.enumdata;

import lombok.Getter;

/**
 * redis中持久存储的数据类型
 * @author fanqie
 * @date 2020/3/2
 */
@Getter
public enum PermanentValueType {

    /* 商品库存 */
    STOCK("stock"),
    /* 商品销量 */
    SALES("sales"),
    ;

    private final String prefix;

    PermanentValueType(final String prefix) {
        this.prefix = prefix;
    }
}
