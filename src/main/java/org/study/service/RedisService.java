package org.study.service;

import java.util.Optional;
import java.util.Set;
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
     * 缓存数据(无本地缓存)
     * @param key 缓存的键值
     * @param data 要缓存的数据
     * @param <T> 缓存数据的类型
     */
    <T> void cacheDataWithoutLocalCache(final String key, final T data);

    /**
     * 得到缓存数据
     * @param key 缓存的键值
     * @param <T> 缓存数据类型
     * @return 缓存数据
     */
    <T> Optional<T> getCache(final String key);

    /**
     * 得到缓存数据(无本地缓存)
     * @param key 缓存的键值
     * @param <T> 缓存数据类型
     * @return 缓存数据
     */
    <T> Optional<T> getCacheWithoutLocalCache(final String key);

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
    void deleteKey(final String key);

    /**
     * 存储字符串数据, 不设置过期时间
     * @param key 键
     * @param value 值
     */
    void saveWithoutExpire(final String key, final Integer value);

    /**
     * key的值增加value
     * @param key 键
     * @param value 要增加的值
     * @return 执行完运算后key的值
     */
    Long increaseKey(final String key, final Integer value);

    /**
     * key的值减少value
     * @param key 键
     * @param value 要减少的值
     * @return 执行完运算后key的值
     */
    Long decreaseKey(final String key, final Integer value);

    /**
     * 插入hash的一个key数据, 若hashKey已经存在则插入失败
     * @param key redis键值
     * @param hashKey hash键值
     * @param val 要插入的值
     * @return 操作是否成功
     */
    Boolean putHashKey(final String key, final String hashKey, final String val);

    /**
     * 删除hash中的key
     * @param key redis键
     * @param hashKeys hash键
     * @return 删除是否成功
     */
    Boolean deleteHashKey(final String key, final Object...hashKeys);

    /**
     * 得到hash的所有key
     * @param key redis键
     * @return hash所有键的集合
     */
    Set<Object> getHashKeys(final String key);

    /**
     * 得到hash对应键的值
     * @param key redis键
     * @param hashKey hash键
     * @return 值
     */
    Object getHashKeyValue(final String key, final String hashKey);

    /**
     * 得到持久存储的string
     * @param key 键值
     * @return 存储值
     */
    Optional<String> getPermanentStr(final String key);

    /**
     * 得到持久存储的数字
     * @param key 键值
     * @return 存储值
     */
    Optional<Integer> getPermanentInt(final String key);

    /**
     * z set 限流
     * @param userId 用户id
     * @param opsType 操作类型
     * @param seconds 时间范围
     * @param maxAllowed 最多做多少次操作
     * @return 是否超出限制
     */
    boolean isMaxAllowed(Integer userId, String opsType, int seconds, int maxAllowed);
}
