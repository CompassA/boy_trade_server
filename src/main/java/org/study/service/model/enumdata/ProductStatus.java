package org.study.service.model.enumdata;

import lombok.Getter;

/**
 * @author fanqie
 * Created on 2020/3/4
 */
@Getter
public enum ProductStatus {
    /** 售卖中 */
    IN_SALE((byte) 0),
    /** 售罄 */
    SOLD_OUT((byte) 1),
    /** 下架 */
    REMOVED((byte) 2),
    ;

    private byte value;

    ProductStatus(final byte value) {
        this.value = value;
    }
}
