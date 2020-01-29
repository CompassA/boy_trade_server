package org.study.service;

import org.study.error.ServerException;
import org.study.model.OrderModel;

import java.util.List;
import java.util.Optional;

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

    /**
     * 根据用户Id查询用户持有的订单
     * @param userId 用户id
     * @return 用户订单
     * @throws ServerException data层转model层失败
     */
    List<OrderModel> selectOrdersByUserId(final Integer userId) throws ServerException;

    /**
     * 根据订单编号查询订单查询订单
     * @param orderId 订单编号
     * @return 订单
     */
    Optional<OrderModel> selectOrderById(final String orderId);
}
