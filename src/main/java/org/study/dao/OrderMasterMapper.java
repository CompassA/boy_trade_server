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

    /**
     * 根据卖家id查询卖家的所有订单
     * @param sellerId 卖家Id
     * @param orderStatus 订单状态
     * @param payStatus 支付状态
     * @return 卖家订单信息
     */
    List<OrderMasterDO> selectBySellerId(@Param("sellerId") final Integer sellerId,
                                         @Param("orderStatus") final Byte orderStatus,
                                         @Param("payStatus") final Byte payStatus);

    /**
     * 更新商品状态
     * @param orderId 订单编号
     * @param orderStatus 要更新的订单状态
     * @param payStatus 要更新的支付状态
     * @return 影响的SQL行数
     */
    int updateStatus(@Param("orderId") final String orderId,
                     @Param("orderStatus") final Byte orderStatus,
                     @Param("payStatus") final Byte payStatus);
}
