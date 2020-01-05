package org.study.error;

import lombok.Getter;

/**
 * @author fanqie
 * @date 2020/1/4
 */
@Getter
public class ServerException extends Exception implements SystemException {

    private final SystemException systemException;

    public ServerException(final SystemException systemException) {
        super();
        this.systemException = systemException;
    }

    @Override
    public Integer getErrCode() {
        return systemException.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return systemException.getErrMsg();
    }
}
