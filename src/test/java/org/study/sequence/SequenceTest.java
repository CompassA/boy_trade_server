package org.study.sequence;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.study.dao.SequenceMapper;
import org.study.data.SequenceDO;

import java.util.concurrent.CountDownLatch;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tomato
 * Created on 2022.12.31
 */
@RunWith(PowerMockRunner.class)
//@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*", "org.w3c.*"})
public class SequenceTest {

    @InjectMocks
    private MysqlSequenceGenerator mockDbSequenceGenerator;

    @Mock
    private SequenceMapper mockMapper;

    private SequenceDO mockSequencePO;

    @Before
    public void init() {
        mockSequencePO = new SequenceDO(1, "mock", 1L, 10L, null, null);
    }

    @After
    public void destroy() {
        mockSequencePO = new SequenceDO(1, "mock", 1L, 10L, null, null);
    }

    /**
     * 测试对内存中的sequence更新是否正常
     */
    @Test
    public void initTest() throws Exception {
        when(mockMapper.selectByPrimaryKey(anyInt())).thenReturn(mockSequencePO);
        when(mockMapper.casUpdateValue(anyInt(), anyLong(), anyLong())).thenReturn(1);

        int threadNum = 30;
        CountDownLatch subWait = new CountDownLatch(1);
        CountDownLatch mainWait = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; ++i) {
            new Thread(() -> {
                try {
                    subWait.await();
                    mockDbSequenceGenerator.nextValue();
                    mainWait.countDown();
                } catch (SequenceException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        subWait.countDown();
        mainWait.await();

        verify(mockMapper, times(3)).selectByPrimaryKey(anyInt());
        verify(mockMapper, times(3)).casUpdateValue(anyInt(), anyLong(), anyLong());
    }

    /**
     * 测试溢出的情况
     */
    @Test
    public void longOverflowTest() {
        mockSequencePO.setValue(Long.MAX_VALUE);
        when(mockMapper.selectByPrimaryKey(anyInt())).thenReturn(mockSequencePO);
        when(mockMapper.casUpdateValue(anyInt(), anyLong(), anyLong())).thenReturn(1);

        boolean hasException = false;
        try {
            mockDbSequenceGenerator.nextValue();
        } catch (SequenceException e) {
            e.printStackTrace();
            hasException = true;
        }

        Assert.assertTrue(hasException);
    }
}
