package org.study.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @author fanqie
 * Created on 2020/1/11
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class ProductSaleDO {

    private Integer id;

    private Integer sales;

    private Integer productId;

    private Timestamp createTime;

    private Timestamp updateTime;
}
