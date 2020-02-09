package org.study.service.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.study.data.ProductDO;

/**
 * @author fanqie
 * @date 2020/2/9
 */
@Getter
@Setter
@Accessors(chain = true)
public class CartDetailModel {

    private ProductDO productDO;

    private Integer numInCart;
}
