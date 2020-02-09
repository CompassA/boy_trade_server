package org.study.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author fanqie
 * @date 2020/2/9
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartDTO {

    private Integer userId;

    private Integer productId;

    private Integer num;

    @Override
    public String toString() {
        return "CartDTO{" +
                "userId=" + userId +
                ", productId=" + productId +
                ", num=" + num +
                '}';
    }
}
