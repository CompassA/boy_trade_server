package org.study.service;

import org.study.error.ServerException;
import org.study.service.model.CartModel;
import org.study.view.CartDTO;

import java.util.List;

/**
 * @author fanqie
 * @date 2020/2/9
 */
public interface CartService {

    /**
     * 往购物车添加一个商品
     * @param cartDTO 用户id, 商品id, 数量
     * @return 操作是否成功 true 成功; false 失败
     */
    Boolean addProduct(final CartDTO cartDTO);

    /**
     * 删除购物车的一个商品
     * @param cartDTO 用户id, 商品id,
     * @return 操作是否成功 true 成功; false 失败
     */
    Boolean deleteProduct(final CartDTO cartDTO);

    /**
     * 删除购物车中的所有商品
     * @param userId 用户id
     * @param productsId 要删除的商品的id
     * @return 操作是否成功 true 成功; false 失败
     */
    Boolean deleteCart(final Integer userId, final List<Integer> productsId);

    /**
     * 得到用户购物车的领域模型
     * @param userId 用户id
     * @return 购物车领域模型
     * @throws ServerException 购物车数据异常
     */
    CartModel getCartModel(final Integer userId) throws ServerException;
}
