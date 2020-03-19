package org.study.service;

import org.study.service.model.OrderLogModel;

import java.util.Map;
import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/3/18
 */
public interface OrderLogService {

    /**
     * 创建扣减日志, 状态redisReduced
     * @param orderId 订单号
     * @param reducedRecord 扣减记录
     * @return 创建成功 true; 失败 false
     */
    boolean createOrderLog(String orderId, Map<Integer, Integer> reducedRecord);

    /**
     * mysql成功扣减, 扣减状态置为mysqlReduced
     * @param orderId 订单编号
     * @return 状态改变成功 true; 失败 false
     */
    boolean mysqlReducedLog(String orderId);

    /**
     * 订单取消, 扣减状态置为canceled
     * @param orderId 订单编号
     * @return 状态改变成功 true; 失败 false
     */
    boolean orderCanceledLog(String orderId);

    /**
     * 单个订单日志查询
     * @param orderId 订单编号
     * @return 状态改变成功 true; 失败 false
     */
    Optional<OrderLogModel> selectByOrderId(String orderId);
}
