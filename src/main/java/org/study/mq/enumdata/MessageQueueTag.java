package org.study.mq.enumdata;

import lombok.Getter;

/**
 * 消息Tag类型
 * @author fanqie
 * @date 2020/3/2
 */
@Getter
public enum MessageQueueTag {

    /** 扣减库存 */
    STOCK_DECREASE("stock_decrease"),
    /** 增加库存 */
    STOCK_INCREASE("stock_increase"),
    /** 减少销量 */
    SALES_DECREASE("sales_decrease"),
    /** 增加销量 */
    SALES_INCREASE("sales_increase"),
    ;

    private String value;

    MessageQueueTag(final String value) {
        this.value = value;
    }
}
