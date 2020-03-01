package org.study.mq.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author fanqie
 * @date 2020/3/1
 */
@Getter
@Setter
@ToString
public class StockMessage {

    private Integer productId;

    private Integer amount;
}
