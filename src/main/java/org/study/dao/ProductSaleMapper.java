package org.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.study.data.ProductSaleDO;

import java.util.List;

/**
 * @author fanqie
 * @date 2020/1/11
 */
@Mapper
public interface ProductSaleMapper {

    /**
     * 初始化商品销量
     * @param productSaleDO 初始化数据
     * @return 更新的语句数
     */
    int initProductSale(@Param("productSale") final ProductSaleDO productSaleDO);

    /**
     * 查询商品销量
     * @param productId 待查询的商品id
     * @return 商品销量信息
     */
    List<ProductSaleDO> selectProductSale(@Param("productIdCollection") final List<Integer> productId);

    /**
     * 单个查询
     * @param productId 商品id
     * @return 商品销量信息
     */
    ProductSaleDO selectSale(@Param("productId") final Integer productId);

    /**
     * 增加商品销量
     * @param productId 商品id
     * @param amount 要增加的数量
     * @return sql影响行数
     */
    int increaseSales(@Param("productId") final Integer productId,
                      @Param("amount") final Integer amount);

    /**
     * 减少商品销量
     * @param productId 商品id
     * @param amount 要减少的数量
     * @return sql影响行数
     */
    int decreaseSales(@Param("productId") final Integer productId,
                      @Param("amount") final Integer amount);
}
