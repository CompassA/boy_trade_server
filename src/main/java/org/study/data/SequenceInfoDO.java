package org.study.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author fanqie
 * @date 2020/1/26
 */
@Getter
@Setter
@Accessors(chain = true)
public class SequenceInfoDO {

    private Integer sequenceId;

    private Integer currentValue;

    private Integer step;

    private Integer minValue;

    private Integer maxValue;

    @Override
    public String toString() {
        return "SequenceInfoDO{" +
                "sequenceId=" + sequenceId +
                ", currentValue=" + currentValue +
                ", step=" + step +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                '}';
    }
}
