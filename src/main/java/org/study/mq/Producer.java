package org.study.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.study.config.MQConfig;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.mq.enumdata.MessageQueueTag;
import org.study.mq.message.MessageFactory;
import org.study.service.OrderService;
import org.study.service.model.OrderModel;
import org.study.service.model.OrderMsgModel;

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

    @Autowired
    private OrderService orderService;

    @PostConstruct
    public void init() throws MQClientException {
        producer = new DefaultMQProducer("producer");
        producer.setNamesrvAddr(mqConfig.getNameServerAddress());
        producer.setSendMsgTimeout(mqConfig.getSendTimeout());
        producer.setRetryTimesWhenSendAsyncFailed(0);
        producer.start();
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderModel createOrder(final OrderModel orderModel) throws ServerException {
        //创建订单, 返回要扣减的库存信息及入库后的订单状态
        final OrderMsgModel orderMsgModel = orderService.createOrder(orderModel);

        try {
            //发送消息扣减库存增加销量
            final Message orderStockMsg = MessageFactory.createOrderStockMsg(
                    mqConfig, orderMsgModel, MessageQueueTag.STOCK_DECREASE_SALES_INCREASE);
            producer.send(orderStockMsg);

            //将入库订单状态返回前端
            return orderMsgModel.getOrderModel();
        } catch (JsonProcessingException | MQClientException | InterruptedException |
                RemotingException | MQBrokerException e) {
            orderService.rollBackStockDecrease(orderMsgModel.getDecreaseRecords());
            throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_SYSTEM_EXCEPTION);
        }
    }

    public boolean asyncDecreaseStock(final Integer productId, final Integer amount) {
        try {
            final Message message = MessageFactory
                    .createStockMsg(mqConfig, productId, amount, MessageQueueTag.STOCK_DECREASE);
            producer.send(message);
        } catch (MQClientException | RemotingException | MQBrokerException |
                InterruptedException | JsonProcessingException e) {
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
