package org.study.test.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.study.config.MyBatisConfiguration;
import org.study.config.SequenceConfiguration;

/**
 * @author Tomato
 * Created on 2022.12.31
 */
public class SpringMockTestBase {

    protected AnnotationConfigApplicationContext context;

    public SpringMockTestBase() {
        this.context = new AnnotationConfigApplicationContext(
                MyBatisConfiguration.class,
                H2DataSourceConfiguration.class,
                SequenceConfiguration.class);
    }
}
