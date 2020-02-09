package org.study.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author fanqie
 * @date 2020/2/9
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartDetailVO {

    private Integer productId;

    private String productName;

    private Integer num;

    private BigDecimal price;

    private String iconUrl;

    private String description;

    @Override
    public String toString() {
        return "CartDetailVO{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", num=" + num +
                ", price=" + price +
                ", iconUrl='" + iconUrl + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
