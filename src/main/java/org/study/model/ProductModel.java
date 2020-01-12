package org.study.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author fanqie
 * @date 2020/1/11
 */
@Getter
@Setter
@Accessors(chain = true)
public class ProductModel {

    private int productId;

    private byte payStatus;

    private int categoryId;

    private int sales;

    private int stock;

    private BigDecimal price;

    private String productName;

    private String description;

    private String iconUrl;

    private Timestamp createTime;

    private Timestamp updateTime;

    @Override
    public String toString() {
        return "ProductModel{" +
                "productId=" + productId +
                ", payStatus=" + payStatus +
                ", categoryId=" + categoryId +
                ", sales=" + sales +
                ", stock=" + stock +
                ", price=" + price +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
