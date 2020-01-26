package org.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.study.data.OrderMasterDO;

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
}
