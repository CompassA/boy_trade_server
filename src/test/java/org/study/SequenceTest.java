package org.study;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.SequenceInfoMapper;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.SequenceService;

/**
 * @author fanqie
 * @date 2020/1/26
 */
public class SequenceTest extends BaseTest {

    private static final Integer TEST_SEQUENCE_ID = 2;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private SequenceInfoMapper sequenceInfoMapper;

    @Test
    @Transactional(rollbackFor = Exception.class)
    @Rollback
    public void sequenceTest() throws ServerException, InterruptedException {
        final String currentSequence = sequenceService.getCurrentValue(TEST_SEQUENCE_ID);
        final Runnable runnable = () -> {
            try {
                System.out.println("generateNewSequence: "
                        + sequenceService.generateNewSequence(TEST_SEQUENCE_ID));
            } catch (ServerException e) {
                e.printStackTrace();
            }
        };
        final Thread t1 = new Thread(runnable);
        final Thread t2 = new Thread(runnable);
        final Thread t3 = new Thread(runnable);
        final Thread t4 = new Thread(runnable);
        final Thread t5 = new Thread(runnable);
        final Thread t6 = new Thread(runnable);
        final Thread t7 = new Thread(runnable);
        final Thread t8 = new Thread(runnable);
        final Thread t9 = new Thread(runnable);

        t1.start(); //999999
        t2.start(); //000000
        t3.start(); //000001
        t4.start(); //000002
        t5.start(); //000003
        t6.start(); //000004
        t7.start(); //000005
        t8.start(); //000006
        t9.start(); //000007

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();
        t7.join();
        t8.join();
        t9.join();

        final String currentThreadSequence = sequenceService.generateNewSequence(TEST_SEQUENCE_ID);
        Assert.assertEquals(currentThreadSequence,
                String.format("%06d", (Integer.parseInt(currentSequence) + 10) % 100000));
    }

    @Test
    public void exceptionTest() {
        try {
            sequenceService.getCurrentValue(-1);
        } catch (final ServerException e) {
            Assert.assertEquals(e.getSystemException(),
                    ServerExceptionBean.SEQUENCE_NOT_EXIST_EXCEPTION);
        }

        try {
            sequenceService.generateNewSequence(-1);
        } catch (final ServerException e) {
            Assert.assertEquals(e.getSystemException(),
                    ServerExceptionBean.SEQUENCE_EXCEPTION);
        }
    }
}
