package org.study.error;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author fanqie
 * @date 2020/1/4
 */
@Getter
public class ServerException extends Exception implements SystemException {

    private final SystemException systemException;

    private String errMsg = StringUtils.EMPTY;

    public ServerException(final SystemException systemException) {
        super();
        this.systemException = systemException;
    }

    public ServerException(final SystemException systemException, final String errMsg) {
        super();
        this.systemException = systemException;
        this.errMsg = errMsg;
    }

    @Override
    public Integer getErrCode() {
        return systemException.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return errMsg.equals(StringUtils.EMPTY)
                ? systemException.getErrMsg()
                : errMsg;
    }
}
