package org.study.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.config.MQConfig;
import org.study.mq.enumdata.MessageQueueTag;
import org.study.mq.message.MessageFactory;

import javax.annotation.PostConstruct;

/**
 * @author fanqie
 * @date 2020/3/1
 */
@Component
public class Producer {

    private DefaultMQProducer producer;

    @Autowired
    private MQConfig mqConfig;

    @PostConstruct
    public void init() throws MQClientException {
        producer = new DefaultMQProducer("producer");
        producer.setNamesrvAddr(mqConfig.getNameServerAddress());
        producer.setSendMsgTimeout(mqConfig.getSendTimeout());
        producer.setRetryTimesWhenSendAsyncFailed(0);
        producer.start();
    }

    public boolean asyncDecreaseStock(final Integer productId, final Integer amount) {
        try {
            final Message message = MessageFactory
                    .createStockMsg(mqConfig, productId, amount, MessageQueueTag.STOCK_DECREASE);
            producer.send(message);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean asyncIncreaseStock(final Integer productId, final Integer amount) {
        try {
            final Message message = MessageFactory
                    .createStockMsg(mqConfig, productId, amount, MessageQueueTag.STOCK_INCREASE);
            producer.send(message);
        }  catch (MQClientException | RemotingException | MQBrokerException |
                InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean asyncIncreaseSale(final Integer productId, final Integer amount) {
        try {
            final Message message = MessageFactory
                    .createSalesMsg(mqConfig, productId, amount, MessageQueueTag.SALES_INCREASE);
            producer.send(message);
        } catch (MQClientException | RemotingException | MQBrokerException |
                InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean asyncDecreaseSale(final Integer productId, final Integer amount) {
        try {
            final Message message = MessageFactory
                    .createSalesMsg(mqConfig, productId, amount, MessageQueueTag.SALES_DECREASE);
            producer.send(message);
        } catch (MQClientException | RemotingException | MQBrokerException |
                InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
