package org.study.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.study.dao.OrderLogMapper;
import org.study.data.OrderLogDO;
import org.study.service.OrderLogService;
import org.study.service.model.OrderLogModel;
import org.study.util.MyStringUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @author fanqie
 * Created on 2020/3/18
 */
@Service
public class OrderLogServiceImpl implements OrderLogService {

    @Resource
    private OrderLogMapper orderLogMapper;

    @Override
    public boolean createOrderLog(String orderId, Map<Integer, Integer> reducedRecord) {
        try {
            return orderLogMapper.insert(orderId, MyStringUtil.mapToByte(reducedRecord)) > 0;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean mysqlReducedLog(String orderId) {
        return orderLogMapper.updateToReduced(orderId) > 0;
    }

    @Override
    public boolean orderCanceledLog(String orderId) {
        return orderLogMapper.updateToCanceled(orderId) > 0;
    }

    @Override
    public Optional<OrderLogModel> selectByOrderId(String orderId) {
        final OrderLogDO orderLogDO = orderLogMapper.selectByOrderId(orderId);
        if (orderLogDO == null) {
            return Optional.empty();
        }
        try {
            final Map<Integer, Integer> reducedMap = MyStringUtil.byteToMap(orderLogDO.getRecord());
            return Optional.of(new OrderLogModel(orderId, reducedMap, orderLogDO.getStatus()));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
