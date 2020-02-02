package org.study.service;

import org.study.model.UserModel;

import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/1/29
 */
public interface SessionService {

    /**
     * 根据令牌判断用户是否登录
     * @param token 用户令牌
     * @return 登录 true；未登录 false
     */
    boolean isLogin(final String token);

    /**
     * 根据令牌获取用户登录信息
     * @param token 用户令牌
     * @return 返回用户信息
     */
    Optional<UserModel> getUserModel(final String token);

    /**
     * 将用户信息放入服务端session
     * @param userModel 用户信息
     * @return 用户token
     */
    String putUserModel(final UserModel userModel);

    /**
     * 将用户令牌销毁
     * @param token 用户令牌
     */
    void logout(final String token);
}
