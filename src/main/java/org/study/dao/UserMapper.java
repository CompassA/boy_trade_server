package org.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.study.data.UserDO;

import java.util.List;
import java.util.Set;

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

    /**
     * 根据姓名查询用户
     * @param name 姓名
     * @return 用户信息
     */
    UserDO selectByName(@Param("name") final String name);

    /**
     * 根据用户名查找用户是否存在
     * @param name 用户名
     * @return 符合查询条件的用户数量
     */
    int selectExistsByName(@Param("name") final String name);

    /**
     * 批量查询用户信息
     * @param userKeys 一组用户主键
     * @return 用户信息
     */
    List<UserDO> queryByKeys(@Param("keys") final Set<Integer> userKeys);
}
