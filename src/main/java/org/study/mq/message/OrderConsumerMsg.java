package org.study.mq.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Map;

/**
 * @author fanqie
 * @date 2020/3/19
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderConsumerMsg {

    private String orderId;

    private Timestamp createTime;

    private Map<Integer, Integer> reducedRecord;

    public OrderConsumerMsg() {
    }

    public OrderConsumerMsg(String orderId, Map<Integer, Integer> reducedRecord,
                            Timestamp createTime) {
        this.orderId = orderId;
        this.reducedRecord = reducedRecord;
        this.createTime = createTime;
    }
}
