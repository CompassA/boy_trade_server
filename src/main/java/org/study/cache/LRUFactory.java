package org.study.cache;

/**
 * @author fanqie
 * Created on 2020/3/11
 */
public final class LRUFactory {

    private static final int LRU_CACHE_SIZE = 10;

    private LRUFactory() {
    }

    public static <K, V> MyCache<K, V> create() {
        return new LocalLRU<>(LRU_CACHE_SIZE);
    }
}
