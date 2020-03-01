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
public class StockQueueConfig {

    @Value("${mq.nameserver.addr}")
    private String nameServerAddress;

    @Value("${mq.topic}")
    private String topicName;

    @Value("${mq.groupname}")
    private String groupName;
}
