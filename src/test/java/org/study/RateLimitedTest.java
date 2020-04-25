package org.study;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.study.service.RedisService;

import java.util.concurrent.ThreadFactory;

/**
 * @author fanqie
 * @date 2020/3/6
 */
public class RateLimitedTest extends BaseTest {

    @Autowired
    private RedisService redisService;

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

    @Test
    public void redisLimitTest() throws InterruptedException {
        final int userId = 10;
        final String opsType = "test";
        final int windowSeconds = 10;
        final int maxLimit = 50;
        int succeed = 0;
        int failed = 0;
        for (int i = 0; i < 100; ++i) {
            boolean res = redisService.isMaxAllowed(userId, opsType, windowSeconds, maxLimit);
            System.out.println(res);
            if (res) {
                succeed++;
            } else {
                failed++;
            }
            Thread.sleep(100);
        }
        Thread.sleep(3000);
        for (int i = 0; i < 100; ++i) {
            boolean res = redisService.isMaxAllowed(userId, opsType, windowSeconds, maxLimit);
            System.out.println(res);
            if (res) {
                succeed++;
            } else {
                failed++;
            }
            Thread.sleep(100);
        }
        System.out.printf("succeed: %d, failed: %d\n", succeed, failed);
    }
}