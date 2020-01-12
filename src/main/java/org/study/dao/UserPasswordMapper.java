package org.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.study.data.UserPasswordDO;

/**
 * @author fanqie
 * @date 2020/1/4
 */
@Mapper
public interface UserPasswordMapper {

    /**
     * 根据主键查询用户密码
     * @param id user主键
     * @return user密码
     */
    UserPasswordDO selectPasswordById(@Param("id") final Integer id);

    /**
     * 插入用户密码
     * @param userPassword 密码信息
     * @return 更新语句数
     */
    int insertPassword(@Param("userPassword") final UserPasswordDO userPassword);
}
