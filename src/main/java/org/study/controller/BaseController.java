package org.study.controller;

import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.study.error.ServerException;
import org.study.error.SystemException;
import org.study.response.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author fanqie
 * @date 2020/1/4
 */
public class BaseController {

    protected static final String CONSUMERS = "application/x-www-form-urlencoded";

    protected static final String USER_MODEL = "userModel";

    protected static final String LOGIN_MARK = "isLogin";

    private static final String ERROR_PROPERTY_NAME = "errorCode";

    private static final String MSG_PROPERTY_NAME = "message";

    /**
     * 通用异常处理
     * ExceptionHandler(Exception.class) 拦截所有Controller层未捕获的异常
     * ResponseStatus(HttpStatus.OK) 将返回码改为200
     * ResponseBody 将异常信息作为HTTP报文的body
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ServerResponse exceptionHandler(
            final HttpServletRequest request, final Exception ex) {
        final Map<String, Object> exceptionData = Maps.newHashMap();
        if (ex instanceof ServerException) {
            final SystemException exception = ((ServerException) ex);
            exceptionData.put(ERROR_PROPERTY_NAME, exception.getErrCode());
            exceptionData.put(MSG_PROPERTY_NAME, exception.getErrMsg());
        } else {
            exceptionData.put(ERROR_PROPERTY_NAME, 1);
            exceptionData.put(MSG_PROPERTY_NAME, ex.getMessage());
        }
        return ServerResponse.create(exceptionData, ServerResponse.FAIL_STATUS);
    }
}
;