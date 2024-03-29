package org.study.service.impl;

import org.springframework.stereotype.Service;
import org.study.service.RedisService;
import org.study.service.SessionService;
import org.study.service.model.UserModel;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author fanqie
 * Created on 2020/1/29
 */
@Service
public class SessionServiceImpl implements SessionService {

    @Resource
    private RedisService service;

    @Override
    public boolean isLogin(final String token, final Integer userId) {
        return this.getUserModel(token)
                .map(model -> model.getUserId().equals(userId))
                .orElse(false);
    }

    @Override
    public Optional<UserModel> getUserModel(final String token) {
        return service.getCache(token);
    }

    @Override
    public String putUserModel(final UserModel userModel) {
        final String token = UUID.randomUUID().toString().replace("-", "");
        service.cacheData(token, userModel);
        service.expire(token, 6, TimeUnit.HOURS);
        return token;
    }

    @Override
    public void logout(final String token) {
        service.deleteKey(token);
    }
}
