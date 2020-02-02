package org.study.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author fanqie
 * @date 2020/2/1
 */
public interface RedisService {

    /**
     * 缓存数据
     * @param key 缓存的键值
     * @param data 要缓存的数据
     * @param <T> 缓存数据的类型
     */
    <T> void cacheData(final String key, final T data);

    /**
     * 得到缓存数据
     * @param key 缓存的键值
     * @param type 要缓存的数据
     * @param <T> 缓存数据类型
     * @return
     */
    <T> Optional<T> getCache(final String key, final Class<T> type);

    /**
     * 为键值设置过期时间
     * @param key 键值
     * @param timeout 过期时间
     * @param unit 过期单位
     */
    void expire(final String key, final long timeout, final TimeUnit unit);

    /**
     * 删除缓存
     * @param key 要删除的键
     */
    void deleteCache(final String key);
}
