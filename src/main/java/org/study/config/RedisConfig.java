package org.study.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @author fanqie
 * Created on 2020/1/29
 */
@Component
public class RedisConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(final RedisConnectionFactory factory) {
        final RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        //设置key序列化方式
        template.setKeySerializer(new StringRedisSerializer());

        //设置value序列化方式
        final SimpleModule simpleModule = new SimpleModule();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        mapper.registerModule(simpleModule);

        final Jackson2JsonRedisSerializer<Object> serializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        serializer.setObjectMapper(mapper);
        template.setValueSerializer(serializer);

        return template;
    }
}
