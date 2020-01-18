package org.study.service;

import org.study.error.ServerException;
import org.study.model.UserModel;

import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/1/4
 */
public interface UserService {

    /**
     * 用户登录
     * @param account 账号
     * @param password 密码
     * @return 用户领域模型数据
     */
    Optional<UserModel> login(final String account, final String password);

    /**
     * 用户注册
     * @param userModel 注册数据
     * @return 注册信息
     * @throws ServerException 注册失败异常
     */
    UserModel registry(final UserModel userModel) throws ServerException;

    /**
     * 查询用户名是否存在
     * @param name 用户名
     * @return 存在 true; 不存在 false
     */
    boolean isUserNameExists(final String name);
}
