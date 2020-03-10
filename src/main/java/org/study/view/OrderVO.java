package org.study.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author fanqie
 * @date 2020/1/26
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class OrderVO {

    private String orderId;

    private Integer userId;

    private Integer sellerId;

    private String userName;

    private String userPhone;

    private String userAddress;

    private Byte payStatus;

    private Byte orderStatus;

    private BigDecimal orderAmount;

    private List<OrderDetailVO> orderDetails;

    private String createTime;

    private String expireTime;

    private String leftTimeInfo;
}
