package org.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.study.data.SequenceInfoDO;

/**
 * @author fanqie
 * Created on 2020/1/26
 */
@Mapper
public interface SequenceInfoMapper {

    /**
     * 加锁查询序列信息
     * @param sequenceId 要查询的序列Id
     * @return 序列所有信息
     */
    SequenceInfoDO selectSequenceInfo(@Param("sequenceId") final Integer sequenceId);

    /**
     * 更新序列
     * @param sequenceId 要更新的序列Id
     * @param newValue 新的序列值
     * @return 影响的行数
     */
    int updateCurrentValue(@Param("sequenceId") final Integer sequenceId,
                           @Param("newValue") final Integer newValue);

    /**
     * 查找目标序列的序列号
     * @param sequenceId 序列id
     * @return 当前序列号
     */
    Integer selectCurrentValue(@Param("sequenceId") final Integer sequenceId);
}
