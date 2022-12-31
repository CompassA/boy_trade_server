package org.study.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author fanqie
 * Created on 2020/5/27
 */
@Aspect
@Component
public class HttpLogAspect {

    @Pointcut("execution(public * org.study.controller.*.*(..))")
    public void log() {
    }

    @Around("log()")
    public Object controllerAround(final ProceedingJoinPoint proceedingJoinPoint) {
        final Signature signature = proceedingJoinPoint.getSignature();
        final String controllerInfo = String.format("Controller Name: %s, Method Name: %s",
                signature.getDeclaringTypeName(), signature.getName());
        System.out.printf("Accept Request, %s\n", controllerInfo);

        Object returnValue = null;
        final long startTime = System.currentTimeMillis();
        try {
            returnValue = proceedingJoinPoint.proceed();
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            final long endTime = System.currentTimeMillis();
            System.out.printf("End of request processing, %s, cost: %dms\n",
                    controllerInfo, endTime - startTime);
        }
        return returnValue;
    }
}
