package org.study.service;

import org.study.error.ServerException;
import org.study.service.model.OrderModel;

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

    /**
     * 根据用户的订单状态查询订单
     * @param userId 用户id
     * @param status 订单状态
     * @return 订单信息
     * @throws ServerException 查询失败
     */
    List<OrderModel> selectByUserIdAndStatus(
            final Integer userId, final Byte status) throws ServerException;

    /**
     * 更新订单状态
     * @param orderId 订单编号
     * @param orderStatus 要更新的订单状态
     * @param payStatus 要更新的支付状态
     * @return 影响的SQL行数
     */
    int updateOrderStatus(final String orderId, final Byte orderStatus, final Byte payStatus);

    /**
     * 查询卖家持有的订单中买方已经支付的部分
     * @param sellerId 卖家id
     * @return 订单信息
     * @throws ServerException 查询失败或转化失败
     */
    List<OrderModel> selectPaidOrderWithSeller(final Integer sellerId) throws ServerException;

    /**
     * 查询卖家已经发货但买家还未收货的订单
     * @param sellerId 卖家id
     * @return 订单信息
     * @throws ServerException 查询失败或转化失败
     */
    List<OrderModel> selectSentOrderWithSeller(final Integer sellerId) throws ServerException;

    /**
     * 查询已经完成交易的订单
     * @param sellerId 卖家id
     * @return 订单信息
     * @throws ServerException 查询失败或转化失败
     */
    List<OrderModel> selectFinishedOrderWithSeller(final Integer sellerId) throws ServerException;
}
