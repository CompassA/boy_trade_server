package org.study.service;

import org.study.data.ProductDO;
import org.study.error.ServerException;
import org.study.service.model.ProductModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author fanqie
 * @date 2020/1/12
 */
public interface ProductService {

    /**
     * 创建商品
     * @param productModel 创建的商品信息
     * @return 数据库商品状态
     * @throws ServerException 商品创建失败
     */
    ProductModel create(final ProductModel productModel) throws ServerException;

    /**
     * 根据主键查询商品信息
     * @param productId 商品主键
     * @return 商品详情
     * @throws ServerException 查询失败
     */
    ProductModel selectByPrimaryKey(final int productId) throws ServerException;

    /**
     * 根据主键查询商品信息(不包含销量和库存)
     * @param productId 商品主键
     * @return 商品详情(不包含销量和库存)
     * @throws ServerException 商品不存在
     */
    ProductModel selectWithoutStockAndSales(final int productId) throws ServerException;

    /**
     * 查询用户发布的商品
     * @param userId 用户id
     * @return 用户发布的所有商品
     * @throws ServerException 查询失败
     */
    List<ProductModel> selectByUserId(final int userId) throws ServerException;

    /**
     * 仅测试用
     * @return 所有的商品
     * @throws ServerException 查询失败
     */
    @Deprecated
    List<ProductModel> getAllProduct() throws ServerException;

    /**
     * 减库存
     * @param productId 要减库存的商品的id
     * @param amount 要减去的数量
     * @return 减库存是否成功 true 成功； false 失败；
     */
    boolean decreaseStock(final Integer productId, final Integer amount);

    /**
     * 增库存
     * @param productId 要增库存的商品的id
     * @param amount 要增加的数量
     * @return 增库存是否成功 true 成功； false 失败；
     */
    boolean increaseStock(final Integer productId, final Integer amount);

    /**
     * 减销量
     * @param productId 要减销量的商品的id
     * @param amount 减去的销量数
     * @return 减销量是否成功 true 成功； false 失败；
     */
    boolean decreaseSales(final Integer productId, final Integer amount);

    /**
     * 增加销量
     * @param productId 要加销量的商品的id
     * @param amount 增加的销量数
     * @return 增加销量是否成功 true 成功； false 失败；
     */
    boolean increaseSales(final Integer productId, final Integer amount);

    /**
     * 减库存增销量(仅redis)
     * @param productId 商品id
     * @param amount 数量
     * @return 操作是否成功 true 成功； false 失败；
     */
    boolean decreaseStockIncreaseSales(final Integer productId, final Integer amount);

    /**
     * 增库存减销量(仅redis)
     * @param productId 商品id
     * @param amount 数量
     * @return 操作是否成功 true 成功； false 失败；
     */
    boolean increaseStockDecreaseSales(final Integer productId, final Integer amount);

    /**
     * 计算商品价格
     * @param productModel 商品领域模型
     * @return 商品当前的实际价格
     */
    BigDecimal getProductPrice(final ProductModel productModel);

    /**
     * 传入一组商品id, 得到一组商品信息(不包含库存与销量)
     * @param productIds 一组商品id
     * @return 一组商品信息
     */
    List<ProductDO> getProductInfoByIds(final List<Integer> productIds);
}
