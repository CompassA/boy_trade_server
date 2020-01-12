package org.study.service;

import org.study.error.ServerException;
import org.study.model.ProductModel;

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
     */
    ProductModel selectByPrimaryKey(final int productId);
}
