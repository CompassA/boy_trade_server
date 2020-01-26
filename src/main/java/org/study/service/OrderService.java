package org.study.service;

import org.study.error.ServerException;
import org.study.model.OrderModel;

/**
 * @author fanqie
 * @date 2020/1/26
 */
public interface OrderService {

    /**
     * 创建订单
     * @param orderModel 前端传过来的下单数据
     * @return 下单完成后的数据
     * @throws ServerException 下单失败
     */
    OrderModel createOrder(final OrderModel orderModel) throws ServerException;
}
