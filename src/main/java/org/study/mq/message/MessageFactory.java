package org.study.mq.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.common.message.Message;
import org.study.config.MQConfig;
import org.study.mq.enumdata.MessageQueueTag;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author fanqie
 * @date 2020/3/1
 */
public final class MessageFactory {

    private MessageFactory() {
    }

    public static Message createStockMsg(final MQConfig config, final Integer productId,
            final Integer amount, final MessageQueueTag tag) throws JsonProcessingException {
        final StockMessage message = new StockMessage().setProductId(productId).setAmount(amount);
        final byte[] body = new ObjectMapper()
                .writeValueAsString(message).getBytes(StandardCharsets.UTF_8);
        return new Message(config.getTopicName(), tag.getValue(), body);
    }

    public static Message createSalesMsg(
            final MQConfig config, final Integer productId,
            final Integer amount, final MessageQueueTag tag) throws JsonProcessingException {
        final SalesMessage message = new SalesMessage().setProductId(productId).setAmount(amount);
        final byte[] body = new ObjectMapper()
                .writeValueAsString(message).getBytes(StandardCharsets.UTF_8);
        return new Message(config.getTopicName(), tag.getValue(), body);
    }

    public static <T> T deserializeMsg(final Message message, final Class<T> type)
            throws IOException {
        return new ObjectMapper().readValue(message.getBody(), type);
    }
}
