package org.study.service.model;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * @author fanqie
 * Created on 2020/3/3
 */
@Getter
@ToString
public class OrderMsgModel {

    /** the order model to be returned to the front-end */
    private final OrderModel orderModel;

    /** productId -> reduced inventory */
    private final Map<Integer, Integer> decreaseRecords;

    private OrderMsgModel(OrderModel orderModel, Map<Integer, Integer> decreaseRecords) {
        this.orderModel = orderModel;
        this.decreaseRecords = decreaseRecords;
    }

    public static InnerBuilder builder() {
        return new InnerBuilder();
    }

    public static class InnerBuilder {

        private OrderModel orderModel;

        private Map<Integer, Integer> decreaseRecords;

        public InnerBuilder orderModel(OrderModel orderModel) {
            this.orderModel = orderModel;
            return this;
        }

        public InnerBuilder decreaseRecords(Map<Integer, Integer> decreaseRecords) {
            this.decreaseRecords = decreaseRecords;
            return this;
        }

        public OrderMsgModel build() {
            return new OrderMsgModel(orderModel, decreaseRecords);
        }
    }
}
