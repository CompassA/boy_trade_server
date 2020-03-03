package org.study.service.model;

import lombok.Getter;

import java.util.Map;

/**
 * @author fanqie
 * @date 2020/3/3
 */
@Getter
public class OrderMsgModel {

    /**
     * 待返回前端的订单model
     */
    private final OrderModel orderModel;

    /**
     * productId -> 购买商品数量
     * 待减库存增销量的商品
     */
    private final Map<Integer, Integer> amountMap;

    private OrderMsgModel(OrderModel orderModel, Map<Integer, Integer> amountMap) {
        this.orderModel = orderModel;
        this.amountMap = amountMap;
    }

    public static InnerBuilder builder() {
        return new InnerBuilder();
    }

    public static class InnerBuilder {

        private OrderModel orderModel;

        private Map<Integer, Integer> amountMap;

        public InnerBuilder orderModel(OrderModel orderModel) {
            this.orderModel = orderModel;
            return this;
        }

        public InnerBuilder amountMap(Map<Integer, Integer> amountMap) {
            this.amountMap = amountMap;
            return this;
        }

        public OrderMsgModel build() {
            return new OrderMsgModel(orderModel, amountMap);
        }
    }
}
