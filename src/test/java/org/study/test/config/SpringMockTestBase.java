package org.study.test.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.study.config.MyBatisConfiguration;

/**
 * @author Tomato
 * Created on 2022.12.31
 */
public class SpringMockTestBase {

    protected AnnotationConfigApplicationContext context;

    public SpringMockTestBase() {
        this.context = new AnnotationConfigApplicationContext(
                MyBatisConfiguration.class,
                H2DataSourceConfiguration.class);
    }
}
