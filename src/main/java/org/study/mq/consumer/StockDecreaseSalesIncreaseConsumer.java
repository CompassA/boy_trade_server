package org.study.mq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.stereotype.Component;
import org.study.config.MQConfig;
import org.study.mq.enumdata.MessageQueueTag;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author fanqie
 * Created on 2020/3/3
 */
@Component
public class StockDecreaseSalesIncreaseConsumer {

    private DefaultMQPushConsumer consumer;

    @Resource
    private MQConfig config;

    @Resource
    private ReduceListener listener;

    @PostConstruct
    public void init() throws MQClientException {
        //组、注册中心、订阅消息
        consumer = new DefaultMQPushConsumer(config.getDecrStockIncrSalesGroup());
        consumer.setNamesrvAddr(config.getNameServerAddress());
        consumer.subscribe(config.getTopicName(),
                MessageQueueTag.STOCK_DECREASE_SALES_INCREASE.getValue());

        //注册回调
        consumer.registerMessageListener(listener);

        //启动
        consumer.start();
    }
}
