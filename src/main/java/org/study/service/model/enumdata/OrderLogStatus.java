package org.study.service.model.enumdata;

import lombok.Getter;

/**
 * @author fanqie
 * Created on 2020/3/18
 */
@Getter
public enum OrderLogStatus {

    /* redis已经扣减 */
    REDIS_REDUCED((byte) 0),
    /* mysql已经扣减 */
    MYSQL_REDUCED((byte) 1),
    /* 订单取消 */
    CANCELED((byte) 2),
    ;

    private byte value;

    OrderLogStatus(byte value) {
        this.value = value;
    }
}
