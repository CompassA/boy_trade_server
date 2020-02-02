package org.study.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.study.service.RedisService;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author fanqie
 * @date 2020/2/1
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public <T> void cacheData(final String key, final T data) {
        redisTemplate.opsForValue().set(key, data);
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
    }

    @Override
    public <T> Optional<T> getCache(final String key, final Class<T> type) {
        try {
            final Object value = redisTemplate.opsForValue().get(key);
            if (value != null && type.equals(value.getClass())) {
                return Optional.of((T) value);
            }
            return Optional.empty();
        } catch (final ClassCastException e) {
            return Optional.empty();
        }
    }

    @Override
    public void expire(final String key, final long timeout, final TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public void deleteCache(final String key) {
        redisTemplate.delete(key);
    }
}
