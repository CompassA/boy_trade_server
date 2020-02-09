package org.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.study.data.ProductDO;

import java.util.List;

/**
 * @author fanqie
 * @date 2020/1/11
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
}
