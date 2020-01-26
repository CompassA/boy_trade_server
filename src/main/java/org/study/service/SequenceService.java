package org.study.service;

import org.study.error.ServerException;

/**
 * @author fanqie
 * @date 2020/1/26
 */
public interface SequenceService {

    Integer DEFAULT_SEQUENCE_ID = 1;

    /**
     * 生成全局唯一的序列号
     * @param sequenceId 根据哪个序列来生成序列号
     * @return 6位序列号
     * @throws ServerException 序列生成失败
     */
    String generateNewSequence(final Integer sequenceId) throws ServerException;

    /**
     * 得到目标序列的当前值
     * @param sequenceId 目标序列的序列号
     * @return 当前序列值
     * @throws ServerException 查找序列失败
     */
    String getCurrentValue(final Integer sequenceId) throws ServerException;
}
