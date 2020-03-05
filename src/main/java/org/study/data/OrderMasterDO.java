package org.study.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author fanqie
 * @date 2020/1/26
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class OrderMasterDO {

    private String orderId;

    private Integer userId;

    private Integer sellerId;

    private String userName;

    private String userPhone;

    private String userAddress;

    private BigDecimal orderAmount;

    private Byte orderStatus;

    private Byte payStatus;

    private Timestamp createTime;

    private Timestamp updateTime;
}
