package org.study.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author fanqie
 * Created on 2020/1/11
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class ProductDO {

    private Integer id;

    private Integer categoryId;

    private Integer userId;

    private Byte status;

    private String name;

    private String description;

    private String iconUrl;

    private BigDecimal price;

    private Timestamp createTime;

    private Timestamp updateTime;
}
