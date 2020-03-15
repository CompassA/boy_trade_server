package org.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.study.data.OrderLogDO;

/**
 * @author fanqie
 * @date 2020/3/15
 */
@Mapper
public interface OrderLogMapper {

    /**
     * 根据订单id查询扣减记录
     * @param orderId 订单编号
     * @return 扣减记录
     */
    OrderLogDO selectByOrderId(@Param("orderId") String orderId);

    /**
     * 插入订单扣减记录
     * @param orderId 订单编号
     * @param record 扣减记录
     * @return 影响行数
     */
    int insert(@Param("orderId") String orderId, @Param("record") byte[] record);

    /**
     * 状态字段置位已扣减
     * @param orderId 订单编号
     * @return 影响行数
     */
    int updateToReduced(@Param("orderId") String orderId);

    /**
     * 状态字段置位已取消
     * @param orderId 订单编号
     * @return 影响行数
     */
    int updateToCanceled(@Param("orderId") String orderId);
}
