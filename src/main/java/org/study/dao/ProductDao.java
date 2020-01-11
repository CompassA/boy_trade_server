package org.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.study.data.ProductDO;

/**
 * @author fanqie
 * @date 2020/1/11
 */
@Mapper
public interface ProductDao {

    /**
     * 插入或更新
     * @param productDO 待插入数据
     * @return insert或update更新的数据行
     */
    int upsertProduct(@Param("product") final ProductDO productDO);
}
