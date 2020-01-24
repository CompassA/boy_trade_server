package org.study.service;

import org.study.error.ServerException;
import org.study.model.ProductModel;

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
     * 仅测试用
     * @return 所有的商品
     * @throws ServerException 查询失败
     */
    @Deprecated
    List<ProductModel> getAllProduct() throws ServerException;
}
