package org.study.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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

    private Object data;

    public static ServerResponse create(final Object data) {
        return create(data, SUCCESS_STATUS);
    }

    public static ServerResponse create(final Object data, final String status) {
        return new ServerResponse().setData(data).setStatus(status);
    }
}
