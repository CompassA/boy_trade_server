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
     */
    UserModel registry(final UserModel userModel) throws ServerException;
}
