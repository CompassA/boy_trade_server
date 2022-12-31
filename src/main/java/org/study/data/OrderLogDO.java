package org.study.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @author fanqie
 * Created on 2020/3/15
 */
@Getter
@Setter
@ToString
public class OrderLogDO {

    private String orderId;

    private byte[] record;

    private Byte status;

    private Timestamp createTime;

    private Timestamp updateTime;
}
