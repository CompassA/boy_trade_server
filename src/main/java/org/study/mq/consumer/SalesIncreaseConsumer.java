package org.study.mq.consumer;

import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;
import org.study.config.MQConfig;
import org.study.dao.ProductSaleMapper;
import org.study.mq.enumdata.MessageQueueTag;
import org.study.mq.message.MessageFactory;
import org.study.mq.message.SalesMessage;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author fanqie
 * Created on 2020/3/2
 */
@Component
@Deprecated
public class SalesIncreaseConsumer {

    private DefaultMQPushConsumer consumer;

    @Resource
    private MQConfig config;

    @Resource
    private ProductSaleMapper saleMapper;

    @PostConstruct
    public void init() throws MQClientException {
        //组、注册中心、订阅信息
        consumer = new DefaultMQPushConsumer(config.getIncrSalesGroup());
        consumer.setNamesrvAddr(config.getNameServerAddress());
        consumer.subscribe(config.getTopicName(), MessageQueueTag.SALES_INCREASE.getValue());

        //注册回调
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @SneakyThrows
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(
                    List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                final SalesMessage msg = MessageFactory
                        .deserializeMsg(list.get(0), SalesMessage.class);
                saleMapper.increaseSales(msg.getProductId(), msg.getAmount());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        //启动
        //consumer.start();
    }
}
