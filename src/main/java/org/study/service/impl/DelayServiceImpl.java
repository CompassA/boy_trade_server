package org.study.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.error.ServerException;
import org.study.service.DelayService;
import org.study.service.OrderService;
import org.study.service.model.DelayTask;
import org.study.util.MyTimeUtil;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.concurrent.DelayQueue;

/**
 * @author fanqie
 * @date 2020/3/10
 */
@Service
public class DelayServiceImpl implements Runnable, DelayService {

    private DelayQueue<DelayTask<String>> tasks;

    @Autowired
    private OrderService orderService;

    @PostConstruct
    public void init () {
        tasks = new DelayQueue<>();
        Thread taskThread = new Thread(this, "order_expire_thread");
        taskThread.start();
    }

    @Override
    public void submitTask(final String orderId, final Timestamp createTime) {
        final long deadline = MyTimeUtil.getDeadlineMills(createTime, OrderServiceImpl.HOUR_PERIOD);
        final DelayTask<String> delayTask = new DelayTask<>(deadline, orderId);
        tasks.offer(delayTask);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final DelayTask<String> task = tasks.take();
                orderService.cancelOrder(task.getTaskMsg());
            } catch (final InterruptedException | ServerException e) {
                e.printStackTrace();
            }
        }
    }
}
