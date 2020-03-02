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

    @Value("${mq.name-server.addr}")
    private String nameServerAddress;

    @Value("${mq.topic-name}")
    private String topicName;

    @Value("${mq.incr-sales-group-name}")
    private String incrSalesGroup;

    @Value("${mq.decr-sales-group-name}")
    private String decrSalesGroup;

    @Value("${mq.incr-stock-group-name}")
    private String incrStockGroup;

    @Value("${mq.decr-stock-group-name}")
    private String decrStockGroup;

    @Value("${mq.send-timeout}")
    private Integer sendTimeout;
}
