package org.study.service;

import org.study.data.ProductDO;
import org.study.error.ServerException;
import org.study.service.model.ProductModel;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author fanqie
 * Created on 2020/1/12
 */
public interface ProductService {

    List<Integer> CATEGORY_LIST = Arrays.asList(7, 6, 5, 4, 3, 2, 1, 0);

    /** 每页有多少商品 */
    int PAGE_SIZE = 12;

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
     * 查询某类别最新的5个商品(不含库存、销量、付款数)
     * @param typeId 类别id
     * @return 商品信息
     */
    List<ProductModel> selectTopFive(final Integer typeId);

    /**
     * redis减库存, 异步通知mysql减库存
     * @param productId 要减库存的商品的id
     * @param amount 要减去的数量
     * @return 减库存是否成功 true 成功； false 失败；
     */
    @Deprecated
    boolean decreaseStock(final Integer productId, final Integer amount);

    /**
     * mysql增库存
     * @param productId 要增库存的商品的id
     * @param amount 要增加的数量
     * @return 增库存是否成功 true 成功； false 失败；
     */
    boolean increaseStock(final Integer productId, final Integer amount);

    /**
     * mysql减销量
     * @param productId 要减销量的商品的id
     * @param amount 减去的销量数
     * @return 减销量是否成功 true 成功； false 失败；
     */
    boolean decreaseSales(final Integer productId, final Integer amount);

    /**
     * redis增加销量, 异步通知mysql增加销量
     * @param productId 要加销量的商品的id
     * @param amount 增加的销量数
     * @return 增加销量是否成功 true 成功； false 失败；
     */
    @Deprecated
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

    /**
     * 将商品下架
     * @param productId 要下架的商品id
     */
    void withdrawFromShelves(final Integer productId);

    /**
     * 商品是否售罄
     * @param id 商品id
     * @return 售罄 true; 未售罄 false
     */
    boolean isSoldOut(final Integer id);

    /**
     * 将商品置为废弃状态，不可再上架
     * @param productId 目标商品id
     */
    void removeProduct(final Integer productId);

    /**
     * 将商品状态改为售卖中
     * @param productId 商品id
     */
    boolean reInSale(final Integer productId);

    /**
     * 查询前pageNum个页的数据
     * @param pageNum pageNum
     * @return 所有页数据
     */
    List<ProductModel> selectFromBegin(final Integer pageNum);

    /**
     * 向后查询页数据
     * @param preLastId 上一个最后一个商品的id
     * @param prePage 上一页页码
     * @param targetPage 目标页
     * @param typeId 类别
     * @return 页数据
     */
    List<ProductModel> selectNextPage(Integer preLastId, Integer prePage, Integer targetPage,
                                      Integer typeId);


    /**
     * 使用limit分页查询数据
     * @param targetPage 目标页码
     * @param typeId 类别
     * @return 页数据
     */
    List<ProductModel> selectPageNormal(Integer targetPage, Integer typeId);

    /**
     * 获取该商品已付款的件数
     * @param id 商品id
     * @return 件数
     */
    Integer getPaidNum(Integer id);

    /**
     * 增加商品付款数
     * @param id 商品id
     * @param amount 要增加的数量
     */
    void increasePaidNum(Integer id, Integer amount);

    /**
     * 是否所有买家都付了款
     * @param id 商品id
     * @return 全都付款 true
     */
    boolean isProductAllPaid(Integer id);

    /**
     * 商品详情缓存失效
     * @param id 商品id
     */
    void delDetailCache(Integer id);
}
