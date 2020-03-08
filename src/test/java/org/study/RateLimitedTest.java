package org.study;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

/**
 * @author fanqie
 * @date 2020/3/6
 */
public class RateLimitedTest extends BaseTest {

    @Test
    public void tokenTest() {
        final RateLimiter rateLimiter = RateLimiter.create(1);
        final long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; ++i) {
            rateLimiter.acquire(1);
        }
        final long endTime = System.currentTimeMillis();
        System.out.printf("%f\n", (endTime - startTime) / 1000.0);
    }
}
