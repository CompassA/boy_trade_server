package org.study.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author fanqie
 * @date 2020/3/15
 */
@Getter
@Setter
@ToString
public class OrderLogDO {

    String orderId;

    byte[] record;

    Byte status;
}
