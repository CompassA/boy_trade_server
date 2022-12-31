package org.study.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author fanqie
 * Created on 2020/3/11
 */
public class LocalLRU<K, V> extends LinkedHashMap<K, V> implements MyCache<K, V> {

    private final int cacheThreshold;

    public LocalLRU(final int cacheThreshold) {
        super((int) Math.ceil(cacheThreshold / 0.75), 0.75f, true);
        this.cacheThreshold = cacheThreshold;
    }

    @Override
    public synchronized V get(Object key) {
        return super.get(key);
    }

    @Override
    public synchronized V put(K key, V value) {
        return super.put(key, value);
    }

    @Override
    public synchronized void invalidate() {
        super.clear();
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return super.size() > cacheThreshold;
    }
}
