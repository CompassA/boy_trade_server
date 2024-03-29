package org.study.sequence;

import org.junit.Assert;
import org.junit.Test;
import org.study.test.config.SpringMockTestBase;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author Tomato
 * Created on 2022.12.31
 */
public class H2MockSequenceTest extends SpringMockTestBase {

    /**
     * 基于H2数据库, 测试并发生成号段
     */
    @Test
    public void concurrentTest() throws InterruptedException {
        // 开启threadNum个线程
        // 每个线程获取targetIdNum个id
        // 测试是否生成了 threadNum * targetIdNum 个id
        Map<Long, Object> usedId = new ConcurrentHashMap<>();
        int threadNum = 100;
        int targetIdNum = 100000;

        MysqlSequenceGenerator sequenceGenerator = context.getBean(MysqlSequenceGenerator.class);

        CountDownLatch threadStart = new CountDownLatch(1);
        CountDownLatch mainAwait = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; ++i) {
            new Thread(() -> {
                try {
                    threadStart.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (int j = 0; j < targetIdNum; ++j) {
                    try {
                        long id = sequenceGenerator.nextValue();
                        usedId.put(id, SequenceTest.class);
                    } catch (SequenceException e) {
                        e.printStackTrace();
                    }
                }

                mainAwait.countDown();
            }).start();
        }
        threadStart.countDown();
        System.out.println("start generate sequence");
        mainAwait.await();

        // 检测数量
        System.out.println("end");
        Assert.assertEquals(usedId.size(), threadNum * targetIdNum);

        // 检测是否连续
        Iterator<Long> it = new TreeSet<>(usedId.keySet()).iterator();
        Long pre = it.next();
        while (it.hasNext()) {
            Long cur = it.next();
            if (cur != pre + 1) {
                System.out.printf("fail: pre = %d, cur = %d\n", pre, cur);
                Assert.fail();
            }
            pre = cur;
        }
    }
}
