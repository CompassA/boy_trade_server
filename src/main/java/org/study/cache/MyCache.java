package org.study.cache;

/**
 * @author fanqie
 * @date 2020/3/11
 */
public interface MyCache<K, V> {

    /**
     * get cache
     * @param key cache key
     * @return cache data
     */
    V get(Object key);

    /**
     * update cache
     * @param key cache key
     * @param value cache value
     * @return expire cache
     */
    V put(K key, V value);

    /**
     * 本地缓存失效
     */
    void invalidate();
}
