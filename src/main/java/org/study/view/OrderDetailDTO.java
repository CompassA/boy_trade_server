package org.study.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailDTO {

    private Integer productId;

    private String productName;

    private Integer productAmount;

    private BigDecimal productPrice;

    private String iconUrl;

    @Override
    public String toString() {
        return "OrderDetailDTO{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productAmount=" + productAmount +
                ", productPrice=" + productPrice +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }
}