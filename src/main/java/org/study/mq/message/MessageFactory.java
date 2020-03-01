package org.study.mq.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author fanqie
 * @date 2020/3/1
 */
public final class MessageFactory {

    private MessageFactory() {
    }

    public static String createStockMsg(final Integer productId, final Integer amount)
            throws JsonProcessingException {
        final StockMessage message = new StockMessage();
        message.setProductId(productId);
        message.setAmount(amount);
        return new ObjectMapper().writeValueAsString(message);
    }

    public static StockMessage deserializeStockMsg(final String msg) throws IOException {
        return new ObjectMapper().readValue(msg, StockMessage.class);
    }
}
