package org.study.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author fanqie
 * @date 2020/3/1
 */
@Component
@Getter
@Setter
public class MQConfig {

    public static final String STOCK_MSG_KEY = "stockMsgMap";

    public static final String ORDER_ID_MSG_KEY = "orderId";

    @Value("${mq.name-server.addr}")
    private String nameServerAddress;

    @Value("${mq.topic-name}")
    private String topicName;

    @Deprecated
    @Value("${mq.incr-sales-group-name}")
    private String incrSalesGroup;

    @Deprecated
    @Value("${mq.decr-sales-group-name}")
    private String decrSalesGroup;

    @Deprecated
    @Value("${mq.incr-stock-group-name}")
    private String incrStockGroup;

    @Deprecated
    @Value("${mq.decr-stock-group-name}")
    private String decrStockGroup;

    @Value("${mq.decr-stock-incr-sales-group-name}")
    private String decrStockIncrSalesGroup;

    @Value("${mq.send-timeout}")
    private Integer sendTimeout;
}
