package org.study.service.api;

import org.study.model.UserModel;

import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/1/4
 */
public interface UserServiceApi {

    /**
     * 用户登录
     * @param account 账号
     * @param password 密码
     * @return 用户领域模型数据
     */
    Optional<UserModel> login(final String account, final String password);
}
