package org.study.cache;

/**
 * @author fanqie
 * @date 2020/3/11
 */
public final class LRUFactory {

    private static final int LRU_CACHE_SIZE = 10;

    private LRUFactory() {
    }

    public static <K, V> MyCache<K, V> getCache() {
        return new LocalLRU<>(LRU_CACHE_SIZE);
    }
}
