package org.study.service.model;

import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.study.data.UserDO;

import java.util.Map;

/**
 * @author fanqie
 * @date 2020/2/9
 */
@Getter
@Setter
@Accessors(chain = true)
public class CartModel {

    private Map<Integer, UserDO> sellerInfoMap;

    private Multimap<Integer, CartDetailModel> productsMap;
}
