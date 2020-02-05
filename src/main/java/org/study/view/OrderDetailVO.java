package org.study.view;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author fanqie
 * @date 2020/1/26
 */
@Getter
@Setter
@Accessors(chain = true)
public class OrderDetailVO {

    private Integer detailId;

    private String orderId;

    private Integer productId;

    private String productName;

    private BigDecimal productPrice;

    private Integer productAmount;

    private String iconUrl;

    @Override
    public String toString() {
        return "OrderDetailVO{" +
                "detailId=" + detailId +
                ", orderId='" + orderId + '\'' +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productAmount=" + productAmount +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }
}
