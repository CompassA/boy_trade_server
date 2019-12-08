package org.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fanqie
 */
@SpringBootApplication(scanBasePackages= {"org.study"})
@MapperScan(basePackages = { "org.study.dao" })
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}
