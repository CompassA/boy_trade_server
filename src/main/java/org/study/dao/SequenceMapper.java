package org.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.study.data.SequenceDO;

/**
 * @author Tomato
 * Created on 2022.12.31
 */
@Mapper
public interface SequenceMapper {

    /**
     * 根据主键查找sequence
     * @param id sequence主键
     * @return sequence数据
     */
    SequenceDO selectByPrimaryKey(int id);

    /**
     * cas方式更新sequence
     * @param id sequence主键
     * @param oldValue 内存中的值
     * @param newValue 要更新的值
     * @return 更新影响的行数
     */
    int casUpdateValue(
            @Param("id") int id,
            @Param("oldValue") long oldValue,
            @Param("newValue") long newValue);
}
