package org.study.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.config.StockQueueConfig;
import org.study.mq.message.MessageFactory;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * @author fanqie
 * @date 2020/3/1
 */
@Component
public class Producer {

    private DefaultMQProducer producer;

    @Autowired
    private StockQueueConfig stockQueueConfig;

    @PostConstruct
    public void init() throws MQClientException {
        producer = new DefaultMQProducer("producer");
        producer.setNamesrvAddr(stockQueueConfig.getNameServerAddress());
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
    }

    public boolean asyncDecreaseStock(final Integer productId, final Integer amount)
            throws JsonProcessingException {
        final String stockMsg = MessageFactory.createStockMsg(productId, amount);
        final Message message = new Message(stockQueueConfig.getNameServerAddress(), "*",
                stockMsg.getBytes(StandardCharsets.UTF_8));
        try {
            producer.send(message);
        } catch (MQClientException | RemotingException |
                MQBrokerException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
