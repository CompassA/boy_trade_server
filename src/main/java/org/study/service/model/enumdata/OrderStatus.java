package org.study.service.model.enumdata;

import lombok.Getter;

/**
 * @author fanqie
 * Created on 2020/2/2
 */
@Getter
public enum OrderStatus {
    /* 取消 */
    CANCELED((byte) -1),
    /* 创建 */
    CREATED((byte) 0),
    /* 已支付 未发货 */
    PAID((byte) 1),
    /* 已支付 已发货 */
    SENT((byte) 2),
    /* 交易完成 */
    FINISHED((byte) 3),
    ;

    private final byte value;

    OrderStatus(final byte value) {
        this.value = value;
    }
}
