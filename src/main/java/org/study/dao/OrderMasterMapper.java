package org.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.study.data.OrderMasterDO;

import java.util.List;

/**
 * @author fanqie
 * @date 2020/1/26
 */
@Mapper
public interface OrderMasterMapper {

    /**
     * 插入订单主数据
     * @param orderMasterDO 待插入的订单主数据
     * @return 影响的sql行数
     */
    int insertOrderMaster(@Param("orderMaster") final OrderMasterDO orderMasterDO);

    /**
     * 根据订单号查询订单
     * @param orderId 订单编号
     * @return 订单信息
     */
    OrderMasterDO selectOrderById(@Param("orderId") final String orderId);

    /**
     * 查询用户的所有订单
     * @param userId 用户Id
     * @return 所有的订单
     */
    List<OrderMasterDO> selectOrdersByUserId(@Param("userId") final Integer userId);
}
