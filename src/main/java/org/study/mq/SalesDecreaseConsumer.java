package org.study.mq;

import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.config.MQConfig;
import org.study.dao.ProductSaleMapper;
import org.study.mq.enumdata.MessageQueueTag;
import org.study.mq.message.MessageFactory;
import org.study.mq.message.SalesMessage;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author fanqie
 * @date 2020/3/2
 */
@Component
public class SalesDecreaseConsumer {

    private DefaultMQPushConsumer consumer;

    @Autowired
    private MQConfig config;

    @Autowired
    private ProductSaleMapper saleMapper;

    @PostConstruct
    public void init() throws MQClientException {
        //组、注册中心、订阅信息
        consumer = new DefaultMQPushConsumer(config.getDecrSalesGroup());
        consumer.setNamesrvAddr(config.getNameServerAddress());
        consumer.subscribe(config.getTopicName(), MessageQueueTag.SALES_DECREASE.getValue());

        //注册回调
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @SneakyThrows
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(
                    List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                final SalesMessage msg = MessageFactory
                        .deserializeMsg(list.get(0), SalesMessage.class);
                saleMapper.decreaseSales(msg.getProductId(), msg.getAmount());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        //启动
        consumer.start();
    }
}
