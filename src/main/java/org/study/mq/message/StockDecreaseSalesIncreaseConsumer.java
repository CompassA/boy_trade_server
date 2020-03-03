package org.study.mq.message;

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
import org.study.dao.ProductStockMapper;
import org.study.mq.enumdata.MessageQueueTag;
import org.study.service.RedisService;
import org.study.service.model.enumdata.CacheType;
import org.study.util.MyStringUtil;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author fanqie
 * @date 2020/3/3
 */
@Component
public class StockDecreaseSalesIncreaseConsumer {

    private DefaultMQPushConsumer consumer;

    @Autowired
    private MQConfig config;

    @Autowired
    private ProductSaleMapper saleMapper;

    @Autowired
    private ProductStockMapper stockMapper;

    @Autowired
    private RedisService redisService;

    @PostConstruct
    public void init() throws MQClientException {
        //组、注册中心、订阅消息
        consumer = new DefaultMQPushConsumer(config.getDecrStockIncrSalesGroup());
        consumer.setNamesrvAddr(config.getNameServerAddress());
        consumer.subscribe(config.getTopicName(),
                MessageQueueTag.STOCK_DECREASE_SALES_INCREASE.getValue());

        //注册回调
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @SneakyThrows
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(
                    List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageFactory.deserializeMapMsg(list.get(0), Integer.class, Integer.class)
                        .forEach((k, v) -> {
                            stockMapper.decreaseStock(k, v);
                            saleMapper.increaseSales(k, v);
                            final String key = MyStringUtil.generateCacheKey(k, CacheType.PRODUCT);
                            redisService.deleteCache(key);
                        });
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        //启动
        consumer.start();
    }
}
