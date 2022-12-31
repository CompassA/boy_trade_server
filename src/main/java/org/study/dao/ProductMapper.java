package org.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.study.data.ProductDO;

import java.util.List;

/**
 * @author fanqie
 * Created on 2020/1/11
 */
@Mapper
public interface ProductMapper {

    /**
     * 插入或更新
     * @param productDO 待插入数据
     * @return insert或update更新的数据行
     */
    int upsertProduct(@Param("product") final ProductDO productDO);

    /**
     * 查询多个商品
     * @param product 商品条件
     * @return 符合条件的商品集合
     */
    List<ProductDO> selectProduct(@Param("product") final ProductDO product);

    /**
     * 查询单个商品
     * @param id 商品主键
     * @return 符合条件的商品
     */
    ProductDO selectByPrimaryKey(@Param("id") final Integer id);

    /**
     * 传入一组商品id, 得到一组商品信息(不包含库存与销量)
     * @param keyList 一组商品id
     * @return 一组商品信息
     */
    List<ProductDO> selectInKeyList(@Param("keyList") final List<Integer> keyList);

    /**
     * 截取表的前n个商品
     * @param n 需要多少商品
     * @return 商品记录
     */
    List<ProductDO> selectFromBegin(@Param("size") int n);

    /**
     * 向后查询页码
     * @param preLastId 上一页最后的商品的id
     * @param gap 上一页最后一个商品与这一页第一个商品之间隔了多少商品
     * @param pageSize 每页有多少商品
     * @param typeId 商品类别
     * @return 页商品记录
     */
    List<ProductDO> selectNextPage(@Param("preLastId") Integer preLastId, @Param("gap") Integer gap,
            @Param("pageSize") Integer pageSize, @Param("typeId") Integer typeId);

    /**
     * 普通分页查询
     * @param gap 间隙
     * @param size 每页有多少商品
     * @param typeId 商品类别
     * @return 页商品记录
     */
    List<ProductDO> selectPageNormal(@Param("gap") Integer gap, @Param("pageSize") Integer size,
                                     @Param("typeId") Integer typeId);

    /**
     * 查询某类别最新的5个商品
     * @param typeId 商品类别
     * @return 记录列表
     */
    List<ProductDO> selectTopFive(@Param("typeId") Integer typeId);
}
