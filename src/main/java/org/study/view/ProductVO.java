package org.study.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author fanqie
 * Created on 2020/1/12
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class ProductVO {

    private Integer productId;

    private Byte payStatus;

    private Integer paidNum;

    private Integer categoryId;

    private Integer sales;

    private Integer stock;

    private BigDecimal price;

    private String productName;

    private String description;

    private String iconUrl;

    private Integer userId;

    private String createTime;

    private String updateTime;
}
