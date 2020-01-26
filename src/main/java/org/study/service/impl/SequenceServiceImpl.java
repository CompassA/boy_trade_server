package org.study.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.SequenceInfoMapper;
import org.study.data.SequenceInfoDO;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.SequenceService;

import java.util.Objects;

/**
 * @author fanqie
 * @date 2020/1/26
 */
@Service
public class SequenceServiceImpl implements SequenceService {

    @Autowired
    private SequenceInfoMapper sequenceInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String generateNewSequence(final Integer sequenceId) throws ServerException {
        final SequenceInfoDO sequenceInfoDO = sequenceInfoMapper.selectSequenceInfo(sequenceId);
        if (Objects.isNull(sequenceInfoDO)) {
            throw new ServerException(ServerExceptionBean.SEQUENCE_EXCEPTION);
        }
        int newValue = sequenceInfoDO.getCurrentValue() + sequenceInfoDO.getStep();
        if (newValue > sequenceInfoDO.getMaxValue()) {
            newValue = sequenceInfoDO.getMinValue();
        }
        sequenceInfoMapper.updateCurrentValue(sequenceId, newValue);
        return String.format("%06d", newValue);
    }

    @Override
    public String getCurrentValue(final Integer sequenceId) throws ServerException {
        final Integer currentValue = sequenceInfoMapper.selectCurrentValue(sequenceId);
        if (currentValue == null) {
            throw new ServerException(ServerExceptionBean.SEQUENCE_NOT_EXIST_EXCEPTION);
        }
        return String.format("%06d", currentValue);
    }
}
