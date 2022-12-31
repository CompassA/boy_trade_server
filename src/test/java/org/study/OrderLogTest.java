package org.study;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.OrderLogMapper;
import org.study.data.OrderLogDO;
import org.study.test.config.SpringMockTestBase;
import org.study.util.MyStringUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fanqie
 * Created on 2020/3/15
 */
public class OrderLogTest extends SpringMockTestBase {

    private final OrderLogMapper orderLogMapper;

    public OrderLogTest() {
        super();
        this.orderLogMapper = this.context.getBean(OrderLogMapper.class);
    }


    @Test
    @Transactional(rollbackFor = Exception.class)
    @Rollback
    public void crudTest() throws IOException {
        final Map<Integer, Integer> record = new HashMap<>(8);
        record.put(1, 1);
        record.put(2, 2);
        record.put(3, 3);
        record.put(4, 4);
        record.put(5, 5);
        final String orderId = "abc";

        Assert.assertEquals(1, orderLogMapper.insert(orderId, MyStringUtil.mapToByte(record)));

        final OrderLogDO data = orderLogMapper.selectByOrderId(orderId);
        Assert.assertNotNull(data);

        Map<Integer, Integer> recordAfterDeserialize = MyStringUtil.byteToMap(data.getRecord());
        System.out.println(recordAfterDeserialize);

        Assert.assertEquals(1, orderLogMapper.updateToReduced(orderId));
        System.out.println(orderLogMapper.selectByOrderId(orderId));

        Assert.assertEquals(1, orderLogMapper.updateToCanceled(orderId));
        System.out.println(orderLogMapper.selectByOrderId(orderId));
    }
}
