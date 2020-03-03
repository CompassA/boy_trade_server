package org.study.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.data.UserDO;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.CartService;
import org.study.service.ProductService;
import org.study.service.RedisService;
import org.study.service.UserService;
import org.study.service.model.CartDetailModel;
import org.study.service.model.CartModel;
import org.study.service.model.enumdata.PermanentValueType;
import org.study.util.MyStringUtil;
import org.study.view.CartDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * key: userId; hashKey: productId; val: num
 * @author fanqie
 * @date 2020/2/9
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Override
    public Boolean addProduct(final CartDTO cartDTO) {
        return redisService.putHashKey(
                MyStringUtil.generatePermanentKey(cartDTO.getUserId(), PermanentValueType.CART),
                cartDTO.getProductId().toString(),
                cartDTO.getNum().toString());
    }

    @Override
    public Boolean deleteProduct(final CartDTO cartDTO) {
        final String key = MyStringUtil
                .generatePermanentKey(cartDTO.getUserId(), PermanentValueType.CART);
        final String productId = cartDTO.getProductId().toString();
        return redisService.deleteHashKey(key, productId);
    }

    @Override
    public Boolean deleteCart(final Integer userId, final List<Integer> productsId) {
        final String key = MyStringUtil.generatePermanentKey(userId, PermanentValueType.CART);
        final Object[] hashKeys = productsId.stream().map(String::valueOf).toArray(Object[]::new);
        return redisService.deleteHashKey(key, hashKeys);
    }

    @Override
    public CartModel getCartModel(final Integer userId) throws ServerException {
        final String key = MyStringUtil.generatePermanentKey(userId, PermanentValueType.CART);
        final Set<Object> hashKeys = redisService.getHashKeys(key);
        if (hashKeys.isEmpty()) {
            return CartModel.EMPTY_CART;
        }
        try {
            //拿到用户购物车所有商品id
            final List<Integer> productIds = hashKeys.stream()
                    .filter(val -> val instanceof String)
                    .map(val -> (String) val)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            //根据商品发布者进行分类
            final Set<Integer> sellerIds = Sets.newHashSet();
            final Multimap<Integer, CartDetailModel> productMap = ArrayListMultimap.create();
            productService.getProductInfoByIds(productIds).forEach(productDO -> {
                final Integer sellerId = productDO.getUserId();
                final String productId = productDO.getId().toString();
                final Integer numInCart = Integer.parseInt(
                        (String) redisService.getHashKeyValue(key, productId));
                final CartDetailModel cartDetailModel = new CartDetailModel()
                        .setNumInCart(numInCart)
                        .setProductDO(productDO);
                productMap.put(sellerId,cartDetailModel);
                sellerIds.add(sellerId);
            });

            //查询商品发布者信息
            final Map<Integer, UserDO> sellerInfoMap = userService.queryByKeys(sellerIds)
                    .stream().collect(Collectors.toMap(
                            UserDO::getUserId, item -> item, (oldVal, newVal) -> newVal));

            return new CartModel()
                    .setSellerInfoMap(sellerInfoMap)
                    .setProductsMap(productMap);
        } catch (final NumberFormatException | ClassCastException e) {
            redisService.deleteKey(key);
            throw new ServerException(ServerExceptionBean.CART_DATA_FORMAT_EXCEPTION);
        }
    }

}
