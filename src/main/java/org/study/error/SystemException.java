package org.study.error;

/**
 * @author fanqie
 * Created on 2020/1/4
 */
public interface SystemException {

    /**
     * 获取错误码
     * @return 错误码
     */
    Integer getErrorCode();

    /**
     * 获取错误描述
     * @return 错误描述
     */
    String getMessage();
}
