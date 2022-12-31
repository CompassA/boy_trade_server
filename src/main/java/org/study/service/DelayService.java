package org.study.service;

import java.sql.Timestamp;

/**
 * @author fanqie
 * Created on 2020/3/10
 */
public interface DelayService {

    /**
     * 提交订单过期定时任务
     * @param orderId 订单号
     * @param createTime 订单创建时间
     */
    void submitTask(final String orderId, final Timestamp createTime);
}
