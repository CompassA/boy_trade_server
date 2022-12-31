package org.study.service.model;

import lombok.Getter;

import java.util.Map;

/**
 * @author fanqie
 * Created on 2020/3/18
 */
@Getter
public class OrderLogModel {

    private String orderId;

    private Map<Integer, Integer> record;

    private Byte status;

    public OrderLogModel(String orderId, Map<Integer, Integer> record, Byte status) {
        this.orderId = orderId;
        this.record = record;
        this.status = status;
    }
}
