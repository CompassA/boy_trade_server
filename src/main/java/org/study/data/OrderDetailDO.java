package org.study.data;

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

    @Override
    public String toString() {
        return "OrderDetailDO{" +
                "detailId=" + detailId +
                ", orderId='" + orderId + '\'' +
                ", ownerId=" + ownerId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productAmount=" + productAmount +
                ", productIcon='" + productIcon + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
