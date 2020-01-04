package org.study.dao;

import org.apache.ibatis.annotations.Param;
import org.study.data.UserPasswordDO;

/**
 * @author fanqie
 * @date 2020/1/4
 */
public interface UserPasswordDao {

    /**
     * 根据主键查询用户密码
     * @param id user主键
     * @return user密码
     */
    UserPasswordDO selectPasswordById(@Param("id") final Integer id);
}
