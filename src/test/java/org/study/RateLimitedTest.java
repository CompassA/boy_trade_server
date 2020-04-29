package org.study;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.study.limiter.BucketLimiter;
import org.study.service.RedisService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

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
        final int windowSeconds = 1;
        final int maxLimit = 2;
        final int threadNum = 10;
        final ExecutorService pool = Executors.newFixedThreadPool(threadNum);
        ((ThreadPoolExecutor) pool).prestartAllCoreThreads();
        for (int i = 1; i <= 10; ++i) {
            final CountDownLatch end = new CountDownLatch(threadNum);
            final int[] succeed = new int[1];
            final int[] failed = new int[1];
            final Runnable task = () -> {
                if (redisService.isMaxAllowed(userId, opsType, windowSeconds, maxLimit)) { ++succeed[0]; }
                else { ++failed[0]; }

                end.countDown();
            };

            for (int j = 0; j < threadNum; ++j) {
                pool.execute(task);
                Thread.sleep(20);
            }
            end.await();
            System.out.printf("test %d: succeed: %d, failed: %d\n", i, succeed[0], failed[0]);
            Thread.sleep(1500);
        }

    }

    @Test
    public void bucketLimiterTest() throws InterruptedException {
        final ExecutorService poolExecutor = Executors.newFixedThreadPool(100);
        final BucketLimiter bucketLimiter = new BucketLimiter(40, 40, 1);
        for (int i = 1; i <= 20; ++i) {
            final int threadNum = 100;
            final int[] succeed = new int[1];
            final int[] failed = new int[1];
            final CountDownLatch start = new CountDownLatch(1);
            final CountDownLatch wait = new CountDownLatch(threadNum);
            final Runnable task = () -> {
                try { start.await(); }
                catch (final InterruptedException e) { e.printStackTrace(); }

                if (bucketLimiter.acquire()) { ++succeed[0]; }
                else { ++failed[0]; }

                wait.countDown();
            };
            for (int j = 0; j < threadNum; ++j) {
                poolExecutor.execute(task);
            }
            start.countDown();
            wait.await();
            System.out.printf("test number: %d, succeed:%d, failed:%d\n", i, succeed[0], failed[0]);
            Thread.sleep(1000);
        }
        poolExecutor.shutdown();
    }
}