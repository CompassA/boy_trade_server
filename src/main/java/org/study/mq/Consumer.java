package org.study.mq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.config.StockQueueConfig;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author fanqie
 * @date 2020/3/1
 */
@Component
public class Consumer {

    private DefaultMQPushConsumer consumer;

    @Autowired
    private StockQueueConfig config;

    @PostConstruct
    public void init() throws MQClientException {
        consumer = new DefaultMQPushConsumer(config.getGroupName());
        consumer.setNamesrvAddr(config.getNameServerAddress());
        consumer.subscribe(config.getTopicName(), "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(
                    List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        });
    }
}
