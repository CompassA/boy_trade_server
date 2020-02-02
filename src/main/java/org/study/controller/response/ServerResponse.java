package org.study.controller.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.study.error.ServerExceptionBean;

/**
 * @author fanqie
 * @date 2020/1/4
 */
@Getter
@Setter
@Accessors(chain = true)
public class ServerResponse {

    public static final String SUCCESS_STATUS = "success";

    public static final String FAIL_STATUS = "fail";

    private String status;

    private Object body;

    public static ServerResponse create(final Object body) {
        return create(body, SUCCESS_STATUS);
    }

    public static ServerResponse fail(final ServerExceptionBean errorInfo) {
        return create(errorInfo, FAIL_STATUS);
    }

    public static ServerResponse create(final Object body, final String status) {
        return new ServerResponse().setBody(body).setStatus(status);
    }
}
