package org.study;

import org.junit.Assert;
import org.junit.Test;
import org.study.cache.LRUFactory;
import org.study.cache.MyCache;

import java.util.List;

/**
 * @author fanqie
 * @date 2020/3/11
 */
public class LRUCacheTest extends BaseTest {

    @Test
    public void initTest() {
        LRUFactory.getCache();
        MyCache<Integer, List<Integer>> cache = LRUFactory.getCache();
        Assert.assertNotNull(cache);
    }

    @Test
    public void cacheTest() {
        MyCache<Integer, Integer> cache = LRUFactory.getCache();
        for (int i = 0; i < 100; ++i) {
            cache.put(i, i);
            Assert.assertNull(cache.get(i - 10));
        }
    }
}
