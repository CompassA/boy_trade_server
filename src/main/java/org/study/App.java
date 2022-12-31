package org.study;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fanqie
 */
@SpringBootApplication(
    scanBasePackages= {"org.study"},
    exclude = {
            MybatisAutoConfiguration.class
    }
)
public class App {
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}
