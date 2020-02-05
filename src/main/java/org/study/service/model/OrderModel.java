package org.study.service.model;

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
public class OrderModel {

    /** 订单号 */
    private String orderId;

    /** 下单用户 */
    private Integer userId;

    /** 卖家 */
    private Integer sellerId;

    /** 用户名称 */
    private String userName;

    /** 用户手机号 */
    private String userPhone;

    /** 用户地址 */
    private String userAddress;

    /** 订单中的商品 */
    private List<OrderDetailModel> productDetails;

    /** 订单总金额 */
    private BigDecimal orderAmount;

    /** 订单状态 */
    private Byte orderStatus;

    /** 支付状态 */
    private Byte payStatus;

    /** 订单创建时间 */
    private Timestamp createTime;

    /** 订单更新时间 */
    private Timestamp updateTime;
}
