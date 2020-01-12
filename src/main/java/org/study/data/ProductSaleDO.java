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
public class ProductSaleDO {

    private Integer id;

    private Integer sales;

    private Integer productId;

    private Timestamp createTime;

    private Timestamp updateTime;

    @Override
    public String toString() {
        return "ProductSaleDO{" +
                "id=" + id +
                ", sales=" + sales +
                ", productId=" + productId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
