package org.study.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.study.aspects.annotation.UserInfoContext;
import org.study.error.ServerException;
import org.study.error.ServerExceptionEnum;
import org.study.service.SessionService;
import org.study.service.model.UserModel;

import javax.annotation.Resource;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.Optional;

/**
 * 校验登录, 并将用户信息放入ThreadLocal
 * @author Tomato
 * Created on 2022.12.31
 */
@Aspect
@Component
public class TokenValidationAspect {

    @Resource
    private SessionService sessionService;

    @Around("@annotation(org.study.aspects.annotation.EnableTokenValidation)")
    public Object controllerAround(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        if (args.length < 2) {
            throw new ServerException(ServerExceptionEnum.TOKEN_VALIDATION_PARAM_FAILURE);
        }

        Parameter[] parameters = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getParameters();
        RequestParam userIdAnnotation = parameters[0].getAnnotation(RequestParam.class);
        RequestParam tokenAnnotation = parameters[1].getAnnotation(RequestParam.class);
        if (userIdAnnotation == null || tokenAnnotation == null) {
            throw new ServerException(ServerExceptionEnum.TOKEN_VALIDATION_PARAM_FAILURE);
        }

        Object firstParam = args[0];
        Object secondParam = args[1];
        if (!(firstParam instanceof Integer) || !(secondParam instanceof String)) {
            throw new ServerException(ServerExceptionEnum.TOKEN_VALIDATION_PARAM_FAILURE);
        }
        Integer userId = (Integer) firstParam;
        String token = (String) secondParam;

        // 获取用户信息并校验
        Optional<UserModel> userModelOpt = sessionService.getUserModel(token);
        if (!userModelOpt.isPresent()) {
            throw new ServerException(ServerExceptionEnum.USER_NOT_LOGIN_EXCEPTION);
        }
        UserModel userModel = userModelOpt.get();
        if (!Objects.equals(userModel.getUserId(), userId)) {
            throw new ServerException(ServerExceptionEnum.USER_NOT_LOGIN_EXCEPTION);
        }

        // 放入线程上下文
        UserInfoContext.set(userModel);
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            UserInfoContext.remove();
        }
    }
}
