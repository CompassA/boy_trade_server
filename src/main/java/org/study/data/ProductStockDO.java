package org.study.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @author fanqie
 * @date 2020/1/11
 */
@Getter
@Setter
@Accessors(chain = true)
public class ProductStockDO {

    private Integer id;

    private Integer stock;

    private Integer productId;

    private Timestamp createTime;

    private Timestamp updateTime;

    @Override
    public String toString() {
        return "ProductStockDO{" +
                "id=" + id +
                ", stock=" + stock +
                ", productId=" + productId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
