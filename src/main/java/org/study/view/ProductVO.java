package org.study.view;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author fanqie
 * @date 2020/1/12
 */
@Getter
@Setter
@Accessors(chain = true)
public class ProductVO {

    private Integer productId;

    private Byte payStatus;

    private Integer categoryId;

    private Integer sales;

    private Integer stock;

    private BigDecimal price;

    private String productName;

    private String description;

    private String iconUrl;

    @Override
    public String toString() {
        return "ProductVO{" +
                "productId=" + productId +
                ", payStatus=" + payStatus +
                ", categoryId=" + categoryId +
                ", sales=" + sales +
                ", stock=" + stock +
                ", price=" + price +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }
}
