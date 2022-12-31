package org.study.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author fanqie
 * Created on 2020/1/26
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class SequenceInfoDO {

    private Integer sequenceId;

    private Integer currentValue;

    private Integer step;

    private Integer minValue;

    private Integer maxValue;
}
