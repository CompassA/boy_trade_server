package org.study.service;

import org.study.error.ServerException;

/**
 * @author fanqie
 * Created on 2020/1/26
 */
public interface SequenceService {

    Integer DEFAULT_SEQUENCE_ID = 1;

    /**
     * 用默认的序列来生成订单号
     * @param userId 用户id
     * @return 8位日期 + 6位序列号 + 2位用户分库分表号
     * @throws ServerException 序列生成失败
     */
    String generateNewSequence(final Integer userId) throws ServerException;

    /**
     * 生成订单号
     * @param sequenceId 根据哪个序列来生成序列号
     * @param userId 用户id
     * @return 8位日期 + 6位序列号 + 2位用户分库分表号
     * @throws ServerException 序列生成失败
     */
    String generateNewSequence(final Integer sequenceId, final Integer userId)
            throws ServerException;

    /**
     * 得到目标序列的当前值
     * @param sequenceId 目标序列的序列号
     * @return 当前序列值
     * @throws ServerException 查找序列失败
     */
    String getCurrentValue(final Integer sequenceId) throws ServerException;
}
