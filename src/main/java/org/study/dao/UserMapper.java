package org.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.study.data.UserDO;

import java.util.List;

/**
 * @author fanqie
 * @date 2019/12/8
 */
@Mapper
public interface UserMapper {

    /**
     * 插入或更新一个用户数据
     * @param user 待插入或更新的用户
     * @return 插入记录的条数
     */
    int upsertUser(@Param("user") final UserDO user);

    /**
     * 查找user信息
     * @param user 要查找的user的信息
     * @return 满足条件的user
     */
    List<UserDO> selectUser(@Param("user") final UserDO user);

    /**
     * 根据账号查找用户信息
     * @param account 账号
     * @return 用户信息
     */
    UserDO selectByAccount(@Param("account") final String account);
}
