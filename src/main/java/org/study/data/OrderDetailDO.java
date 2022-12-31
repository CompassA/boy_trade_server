package org.study.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author fanqie
 * Created on 2020/1/26
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class OrderDetailDO {

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
