package org.study.service.model;

import lombok.Getter;
import lombok.Setter;
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
public class OrderDetailModel {

    private Integer detailId;

    private String orderId;

    private Integer ownerId;

    private Integer productId;

    private String productName;

    private BigDecimal productPrice;

    private Integer productAmount;

    private String productIcon;

    private Timestamp createTime;

    private Timestamp updateTime;
}
