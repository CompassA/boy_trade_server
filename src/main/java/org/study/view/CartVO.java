package org.study.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author fanqie
 * @date 2020/2/9
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartVO {

    private String sellerName;

    private Integer sellerId;

    private List<CartDetailVO> cartDetails;

    @Override
    public String toString() {
        return "CartVO{" +
                "sellerName='" + sellerName + '\'' +
                ", sellerId=" + sellerId +
                ", cartDetails=" + cartDetails +
                '}';
    }
}
