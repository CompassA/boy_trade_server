package org.study.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.study.service.RedisService;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author fanqie
 * @date 2020/2/1
 */
@Service
public class RedisServiceImpl implements RedisService {

    private Cache<String, Object> localCache;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @PostConstruct
    public void init() {
        localCache = CacheBuilder.newBuilder()
                .initialCapacity(10)
                .maximumSize(100)
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public <T> void cacheData(final String key, final T data) {
        localCache.put(key, data);
        redisTemplate.opsForValue().set(key, data);
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
    }

    @Override
    public <T> void cacheDataWithoutLocalCache(final String key, final T data) {
        redisTemplate.opsForValue().set(key, data);
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
    }

    @Override
    public <T> Optional<T> getCache(final String key) {
        if (key == null) {
            return Optional.empty();
        }
        try {
            //先获取本地内存
            Object value = localCache.getIfPresent(key);
            if (value != null) {
                return Optional.of((T) value);
            }
            //本地内存不存在 获取redis缓存
            value = redisTemplate.opsForValue().get(key);
            if (value != null) {
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
    public <T> Optional<T> getCacheWithoutLocalCache(final String key) {
        final Object cache = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable((T) cache);
    }

    @Override
    public void expire(final String key, final long timeout, final TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public void deleteKey(final String key) {
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

    @Override
    public Boolean putHashKey(final String key, final String hashKey, final String val) {
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, val);
    }

    @Override
    public Boolean deleteHashKey(final String key, final Object...hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys) > 0;
    }

    @Override
    public Set<Object> getHashKeys(final String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    @Override
    public Object getHashKeyValue(final String key, final String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Optional<String> getPermanentStr(final String key) {
        final Object res = redisTemplate.opsForValue().get(key);
        return res instanceof String ? Optional.of((String) res) : Optional.empty();
    }

    @Override
    public Optional<Integer> getPermanentInt(final String key) {
        final Object res = redisTemplate.opsForValue().get(key);
        return res instanceof Integer ? Optional.of((Integer) res) : Optional.empty();
    }

    @Override
    public boolean isMaxAllowed(final Integer userId, final String opsType,
            final int seconds, final int maxOps) {
        final String opsKey = String.format("limit:%d:%s", userId, opsType);
        final long curTime = System.currentTimeMillis();

        final List<Object> res = redisTemplate.executePipelined((RedisConnection connection) -> {
            final byte[] key = opsKey.getBytes();
            connection.zAdd(key, curTime, Long.toString(curTime).getBytes());
            connection.expire(key, seconds + 1);
            connection.zRemRangeByScore(key, 0, curTime - seconds * 1000);
            connection.zCard(key);
            return null;
        });

        final int cardPos = 3;
        return maxOps >= Optional.ofNullable(res.get(cardPos)).map(obj -> (Long) obj).orElse(0L);
    }
}
