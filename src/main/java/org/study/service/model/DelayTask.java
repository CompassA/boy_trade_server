package org.study.service.model;

import lombok.Getter;

import javax.annotation.Nonnull;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author fanqie
 * Created on 2020/3/10
 */
@Getter
public class DelayTask<T> implements Delayed {

    private final long deadlineMills;

    private final T taskMsg;

    public DelayTask(final long deadlineMills, final T taskMsg) {
        this.deadlineMills = deadlineMills;
        this.taskMsg = taskMsg;
    }

    @Override
    public long getDelay(@Nonnull TimeUnit unit) {
        return deadlineMills - System.currentTimeMillis();
    }

    @Override
    public int compareTo(@Nonnull Delayed other) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS));
    }
}
