package org.study.view;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author fanqie
 * @date 2020/1/26
 */
@Getter
@Setter
@Accessors(chain = true)
public class OrderVO {

    private String orderId;

    private Integer userId;

    private String userName;

    private String userPhone;

    private String userAddress;

    private Byte payStatus;

    private Byte orderStatus;

    private BigDecimal orderAmount;

    private List<OrderDetailVO> orderDetails;

    private Timestamp createTime;

    @Override
    public String toString() {
        return "OrderVO{" +
                "orderId='" + orderId + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", payStatus=" + payStatus +
                ", orderStatus=" + orderStatus +
                ", orderAmount=" + orderAmount +
                ", orderDetails=" + orderDetails +
                ", createTime=" + createTime +
                '}';
    }
}