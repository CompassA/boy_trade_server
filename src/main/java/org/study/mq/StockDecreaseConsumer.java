package org.study.mq;

import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.config.MQConfig;
import org.study.dao.ProductStockMapper;
import org.study.mq.enumdata.MessageQueueTag;
import org.study.mq.message.MessageFactory;
import org.study.mq.message.StockMessage;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author fanqie
 * @date 2020/3/1
 */
@Component
public class StockDecreaseConsumer {

    private DefaultMQPushConsumer consumer;

    @Autowired
    private MQConfig config;

    @Autowired
    private ProductStockMapper stockMapper;

    @PostConstruct
    public void init() throws MQClientException {
        //组、注册中心、订阅信息
        consumer = new DefaultMQPushConsumer(config.getDecrStockGroup());
        consumer.setNamesrvAddr(config.getNameServerAddress());
        consumer.subscribe(config.getTopicName(), MessageQueueTag.STOCK_DECREASE.getValue());

        //注册回调方法
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @SneakyThrows
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(
                    List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                final StockMessage decreaseMsg = MessageFactory
                        .deserializeMsg(list.get(0), StockMessage.class);
                stockMapper.decreaseStock(decreaseMsg.getProductId(), decreaseMsg.getAmount());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        //启动
        consumer.start();
    }
}
