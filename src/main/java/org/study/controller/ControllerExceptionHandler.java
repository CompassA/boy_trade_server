package org.study.controller;

import com.google.common.collect.Maps;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.study.controller.response.ServerResponse;
import org.study.error.ServerException;
import org.study.error.SystemException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author fanqie
 * @date 2020/1/29
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    public static final String ERROR_PROPERTY_NAME = "errorCode";

    public static final String MSG_PROPERTY_NAME = "message";

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ServerResponse handle(final HttpServletRequest request,
            final HttpServletResponse response, final Exception ex) {
        final Map<String, Object> exceptionData = Maps.newHashMap();
        //业务异常
        if (ex instanceof ServerException) {
            final SystemException exception = ((ServerException) ex);
            exceptionData.put(ERROR_PROPERTY_NAME, exception.getErrorCode());
            exceptionData.put(MSG_PROPERTY_NAME, exception.getMessage());
        }
        //405
        else if (ex instanceof ServletRequestBindingException) {
            exceptionData.put(ERROR_PROPERTY_NAME, 1);
            exceptionData.put(MSG_PROPERTY_NAME, "url路由绑定存在问题");
        }
        //404
        else if (ex instanceof NoHandlerFoundException) {
            exceptionData.put(ERROR_PROPERTY_NAME, 1);
            exceptionData.put(MSG_PROPERTY_NAME, "没有找到对应的访问路径");
        }
        //其他异常
        else {
            exceptionData.put(ERROR_PROPERTY_NAME, 1);
            exceptionData.put(MSG_PROPERTY_NAME, "未知错误");
        }
        return ServerResponse.create(exceptionData, ServerResponse.FAIL_STATUS);
    }
}
