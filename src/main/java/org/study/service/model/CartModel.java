package org.study.service.model;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@ToString
public class CartModel {

    public static final CartModel EMPTY_CART = new CartModel()
            .setProductsMap(ArrayListMultimap.create())
            .setSellerInfoMap(Maps.newHashMap());

    private Map<Integer, UserDO> sellerInfoMap;

    private Multimap<Integer, CartDetailModel> productsMap;
}
