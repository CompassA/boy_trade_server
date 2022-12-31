package org.study.error;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author fanqie
 * Created on 2020/1/4
 */
@Getter
public class ServerException extends Exception implements SystemException {

    private final SystemException systemException;

    private String message = StringUtils.EMPTY;

    public ServerException(final SystemException systemException) {
        super();
        this.systemException = systemException;
    }

    public ServerException(final SystemException systemException, final String message) {
        super();
        this.systemException = systemException;
        this.message = message;
    }

    @Override
    public Integer getErrorCode() {
        return systemException.getErrorCode();
    }

    @Override
    public String getMessage() {
        return this.message.equals(StringUtils.EMPTY)
                ? systemException.getMessage()
                : this.message;
    }
}
