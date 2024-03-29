package org.study.mq.consumer;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.ProductSaleMapper;
import org.study.dao.ProductStockMapper;
import org.study.mq.message.MessageFactory;
import org.study.mq.message.OrderConsumerMsg;
import org.study.service.DelayService;
import org.study.service.OrderLogService;
import org.study.service.RedisService;
import org.study.service.model.enumdata.CacheType;
import org.study.util.MyStringUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author fanqie
 * Created on 2020/3/19
 */
@Component
public class ReduceListener implements MessageListenerConcurrently {

    @Resource
    private ProductSaleMapper saleMapper;

    @Resource
    private ProductStockMapper stockMapper;

    @Resource
    private RedisService redisService;

    @Resource
    private OrderLogService orderLogService;

    @Resource
    private DelayService delayService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ConsumeConcurrentlyStatus consumeMessage(
            final List<MessageExt> msgs, final ConsumeConcurrentlyContext context) {
        try {
            final OrderConsumerMsg msg = MessageFactory.deserializeMsg(
                    msgs.get(0), OrderConsumerMsg.class);
            for (final Map.Entry<Integer, Integer> entry : msg.getReducedRecord().entrySet()) {
                final Integer productId = entry.getKey();
                final Integer amount = entry.getValue();
                if (stockMapper.decreaseStock(productId, amount) < 1
                        || saleMapper.increaseSales(productId, amount) < 1) {
                    throw new Exception();
                }
                final String key = MyStringUtil.getCacheKey(productId, CacheType.PRODUCT);
                redisService.deleteKey(key);
            }

            if (!orderLogService.mysqlReducedLog(msg.getOrderId())) {
                throw new Exception();
            }

            //cancel order after 2 hours
            delayService.submitTask(msg.getOrderId(), msg.getCreateTime());

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}
