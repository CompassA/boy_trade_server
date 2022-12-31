package org.study.aspects.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * token校验
 * 条件:
 * 1.userId是函数的第一个参数
 * 2.token是函数的第二个参数
 * 3.userId\token参数有@RequestParam注解
 *
 *
 * 主要是为了兼容之前不太好的设计(没有把token\ userId 放在http header里)
 * 不想改前端代码
 * @author Tomato
 * Created on 2022.12.31
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EnableTokenValidation {
}
