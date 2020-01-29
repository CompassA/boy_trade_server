package org.study.service;

import org.study.model.UserModel;

import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/1/29
 */
public interface SessionService {

    /**
     * 判断用户是否登录
     * @return 登录 true；未登录 false
     */
    boolean isLogin();

    /**
     * 获取当前sessionId对应的用户信息
     * @return 返回用户信息
     */
    Optional<UserModel> getUserModel();

    /**
     * 将用户信息放入服务端session
     * @param userModel 用户信息
     */
    void putUserModel(final UserModel userModel);

    /**
     * 登出
     */
    void logout();
}
