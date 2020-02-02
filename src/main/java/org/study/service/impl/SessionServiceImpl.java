package org.study.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.model.UserModel;
import org.study.service.RedisService;
import org.study.service.SessionService;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author fanqie
 * @date 2020/1/29
 */
@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private RedisService service;

    @Override
    public boolean isLogin(final String token) {
        return service.getCache(token, UserModel.class).isPresent();
    }

    @Override
    public Optional<UserModel> getUserModel(final String token) {
        return service.getCache(token, UserModel.class);
    }

    @Override
    public String putUserModel(final UserModel userModel) {
        final String token = UUID.randomUUID().toString().replace("-", "");
        service.cacheData(token, userModel);
        service.expire(token, 1, TimeUnit.HOURS);
        return token;
    }

    @Override
    public void logout(final String token) {
        service.deleteCache(token);
    }
}
