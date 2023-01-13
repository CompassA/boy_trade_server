package org.study.aspects;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestParam;
import org.study.aspects.annotation.EnableTokenValidation;
import org.study.aspects.annotation.UserInfoContext;
import org.study.error.ServerException;
import org.study.error.ServerExceptionEnum;
import org.study.service.SessionService;
import org.study.service.model.UserModel;
import org.study.util.BizReflectionUtils;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tomato
 * Created on 2022.12.31
 */
@RunWith(PowerMockRunner.class)
@SuppressWarnings("all")
public class TokenValidationAspectTest {

    @Mock
    private SessionService sessionService;

    @Test
    public void testValidation() throws Exception {
        when(sessionService.getUserModel(any())).thenReturn(Optional.of(new UserModel().setUserId(1)));

        // 创建spring容器
        AnnotationConfigApplicationContext context = createContext();
        TokenValidationAspect aspect = context.getBean(TokenValidationAspect.class);
        BizReflectionUtils.reflectSet(aspect, TokenValidationAspect.class, "sessionService", sessionService);

        // 正常情况
        TokenValidationTestClass bean = context.getBean(TokenValidationTestClass.class);
        Assert.assertEquals("1", bean.test(1, "token"));
        Assert.assertNull(UserInfoContext.get());

        // 用户未登录
        when(sessionService.getUserModel(any())).thenReturn(Optional.empty());
        ServerException targetException = null;
        try {
            bean.test(2, "token");
        } catch (UndeclaredThrowableException e) {
            if (e.getUndeclaredThrowable() instanceof ServerException) {
                targetException = (ServerException) e.getUndeclaredThrowable();
            }
        }
        Assert.assertNotNull(targetException);
        Assert.assertEquals(targetException.getSystemException(), ServerExceptionEnum.USER_NOT_LOGIN_EXCEPTION);

        // 参数错误1
        targetException = null;
        try {
            bean.testFail1(2, "token");
        } catch (UndeclaredThrowableException e) {
            if (e.getUndeclaredThrowable() instanceof ServerException) {
                targetException = (ServerException) e.getUndeclaredThrowable();
            }
        }
        Assert.assertNotNull(targetException);
        Assert.assertEquals(targetException.getSystemException(), ServerExceptionEnum.TOKEN_VALIDATION_PARAM_FAILURE);

        // 参数错误2
        targetException = null;
        try {
            bean.testFail2("", 3L);
        } catch (UndeclaredThrowableException e) {
            if (e.getUndeclaredThrowable() instanceof ServerException) {
                targetException = (ServerException) e.getUndeclaredThrowable();
            }
        }
        Assert.assertNotNull(targetException);
        Assert.assertEquals(targetException.getSystemException(), ServerExceptionEnum.TOKEN_VALIDATION_PARAM_FAILURE);

        // 参数错误3
        targetException = null;
        try {
            bean.testFail3();
        } catch (UndeclaredThrowableException e) {
            if (e.getUndeclaredThrowable() instanceof ServerException) {
                targetException = (ServerException) e.getUndeclaredThrowable();
            }
        }
        Assert.assertNotNull(targetException);
        Assert.assertEquals(targetException.getSystemException(), ServerExceptionEnum.TOKEN_VALIDATION_PARAM_FAILURE);

    }

    private AnnotationConfigApplicationContext createContext() {
        return new AnnotationConfigApplicationContext(
                TokenValidationTestClass.class,
                TokenValidationAspect.class,
                sessionService.getClass()
        );
    }

    @EnableAspectJAutoProxy
    public static class TokenValidationTestClass {

        @EnableTokenValidation
        public String test(
                @RequestParam Integer userId,
                @RequestParam String token) {
            UserModel userModel = UserInfoContext.get();
            return userModel.getUserId().toString();
        }

        @EnableTokenValidation
        public String testFail1(Integer UserId, String token) {
            return "";
        }

        @EnableTokenValidation
        public String testFail2(
                @RequestParam String token,
                @RequestParam Long userId) {
            return "";
        }

        @EnableTokenValidation
        public String testFail3() {
            return "";
        }
    }

}
