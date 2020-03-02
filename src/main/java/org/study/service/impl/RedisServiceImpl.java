package org.study.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.study.service.RedisService;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author fanqie
 * @date 2020/2/1
 */
@Service
public class RedisServiceImpl implements RedisService {

    private Cache<String, Object> localCache;

    @PostConstruct
    public void init() {
        localCache = CacheBuilder.newBuilder()
                .initialCapacity(10)
                .maximumSize(100)
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build();
    }

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public <T> void cacheData(final String key, final T data) {
        localCache.put(key, data);
        redisTemplate.opsForValue().set(key, data);
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
    }

    @Override
    public <T> void cacheDataWithoutLocalCache(String key, T data) {
        redisTemplate.opsForValue().set(key, data);
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
    }

    @Override
    public <T> Optional<T> getCache(final String key, final Class<T> type) {
        try {
            //先获取本地内存
            Object value = localCache.getIfPresent(key);
            if (value != null && type.equals(value.getClass())) {
                return Optional.of((T) value);
            }
            //本地内存不存在 获取redis缓存
            value = redisTemplate.opsForValue().get(key);
            if (value != null && type.equals(value.getClass())) {
                //更新本地缓存
                localCache.put(key, value);
                return Optional.of((T) value);
            }
            return Optional.empty();
        } catch (final ClassCastException e) {
            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<T> getCacheWithoutLocalCache(String key, Class<T> type) {
        final Object cache = redisTemplate.opsForValue().get(key);
        if (cache != null && type.equals(cache.getClass())) {
            return Optional.of((T) cache);
        }
        return Optional.empty();
    }

    @Override
    public void expire(final String key, final long timeout, final TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public void deleteCache(final String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void saveWithoutExpire(final String key, final Integer value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Long increaseKey(final String key, final Integer value) {
        return redisTemplate.opsForValue().increment(key, value);
    }

    @Override
    public Long decreaseKey(final String key, final Integer value) {
        return redisTemplate.opsForValue().decrement(key, value);
    }
}
