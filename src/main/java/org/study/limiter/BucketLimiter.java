package org.study.limiter;

import lombok.Getter;

/**
 * @author fanqie
 * @date 2020/4/29
 */
@Getter
public class BucketLimiter {

    private final int capacity;
    private final double speed;
    private int availableSpace;
    private long lastTimeMillis;

    public BucketLimiter(final int capacity, final int maxOps, final int seconds) {
        this.availableSpace = this.capacity = capacity;
        this.speed = 1.0 * maxOps / (seconds * 1000);
        this.lastTimeMillis = System.currentTimeMillis();
    }

    public synchronized boolean acquire() {
        final long curTimeMillis = System.currentTimeMillis();
        final double deltaMillis = curTimeMillis - lastTimeMillis;
        if (deltaMillis < 0) {
            throw new RuntimeException("illegal time");
        }
        final int leakedNum = (int) Math.round(speed * deltaMillis);
        availableSpace = Math.min(capacity, availableSpace + leakedNum);
        if (availableSpace - 1 < 0) {
            return false;
        }
        availableSpace -= 1;
        lastTimeMillis = curTimeMillis;
        return true;
    }
}
