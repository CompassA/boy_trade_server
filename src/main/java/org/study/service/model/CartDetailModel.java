package org.study.service.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.study.data.ProductDO;

/**
 * @author fanqie
 * Created on 2020/2/9
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class CartDetailModel {

    private ProductDO productDO;

    private Integer numInCart;
}
