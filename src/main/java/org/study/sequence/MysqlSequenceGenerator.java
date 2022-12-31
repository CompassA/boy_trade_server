package org.study.sequence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.study.dao.SequenceMapper;
import org.study.data.SequenceDO;

import java.util.Optional;

/**
 * 通过MySQL生成分布式id
 * @author Tomato
 * Created on 2022.12.31
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MysqlSequenceGenerator extends BaseSequenceGenerator {

    /**
     * 序列id, 对应sequence表中的一行数据的主键
     */
    private int dbSequenceKey;

    /**
     * 读写sequence表
     */
    private SequenceMapper mapper;

    @Override
    public String getSequenceId() {
        return MysqlSequenceGenerator.class.getSimpleName();
    }

    @Override
    protected Optional<SequenceRange> fetchSequence() throws SequenceException {
        for (int i = 0; i < 1000000; ++i) {
            SequenceDO sequenceDO = mapper.selectByPrimaryKey(dbSequenceKey);
            long oldValue = sequenceDO.getValue();
            long newValue = oldValue + sequenceDO.getStep();
            if (mapper.casUpdateValue(dbSequenceKey, oldValue, newValue) < 1) {
                continue;
            }
            // 溢出
            if (newValue < oldValue) {
                throw new SequenceException(
                        String.format("sequence overflow, sequence key: [%s]", dbSequenceKey));
            }
            return Optional.of(new SequenceRange(oldValue + 1, newValue));
        }
        return Optional.empty();
    }
}
