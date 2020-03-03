package org.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.study.data.OrderDetailDO;

import java.util.List;

/**
 * @author fanqie
 * @date 2020/1/26
 */
@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入订单元素
     * @param details 订单元素
     * @return 受影响的sql行数
     * @throws DataAccessException 插入失败
     */
    int insertOrderDetails(@Param("details") final List<OrderDetailDO> details)
            throws DataAccessException;

    /**
     * 根据订单号查询订单元素
     * @param orderId 订单编号
     * @return 订单元素列表
     */
    List<OrderDetailDO> selectDetailByOrderId(@Param("orderId") final String orderId);
}
