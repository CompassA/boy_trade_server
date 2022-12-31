package org.study.test.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Tomato
 * Created on 2022.12.31
 */
@Configuration
public class H2DataSourceConfiguration {

    private static final String H2_DRIVER_CLASS_NAME = "org.h2.Driver";
    private static final String H2_URL = "jdbc:h2:mem:trade;DB_CLOSE_DELAY=-1;MODE=MySQL;INIT=runscript from 'classpath:sql/trade.sql'";

    @Bean
    public DataSource h2DataSource() {
        return DataSourceBuilder.create()
                .driverClassName(H2_DRIVER_CLASS_NAME)
                .url(H2_URL)
                .type(DruidDataSource.class)
                .build();
    }

}
