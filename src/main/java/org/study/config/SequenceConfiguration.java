package org.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.study.dao.SequenceMapper;
import org.study.sequence.MysqlSequenceGenerator;
import org.study.sequence.SequenceGenerator;

/**
 * @author Tomato
 * Created on 2022.12.31
 */
@Configuration
public class SequenceConfiguration {

    private static final int ORDER_SEQUENCE_KEY = 1;

    @Bean
    public SequenceGenerator dbSequenceGenerator(SequenceMapper mapper) {
        return new MysqlSequenceGenerator(ORDER_SEQUENCE_KEY, mapper);
    }
}
