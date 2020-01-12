package org.study.dao;

import org.apache.ibatis.annotations.Param;
import org.study.data.ProductStockDO;

import java.util.List;

/**
 * @author fanqie
 * @date 2020/1/11
 */
public interface ProductStockMapper {

    /**
     * 初始化商品库存
     * @param productStockDO 库存信息
     * @return 更新语句数
     */
    int initProductStock(@Param("productStock") final ProductStockDO productStockDO);

    /**
     * 批量查询商品库存
     * @param productId 商品id集合
     * @return 库存信息
     */
    List<ProductStockDO> selectProductStock(@Param("productIdCollection") final List<Integer> productId);
}
