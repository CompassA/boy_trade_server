package org.study.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fanqie
 * @date 2020/1/13
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerRequest {

    private String key;

    private String encryptData;

    public ServerRequest(final String key, final String encryptData) {
        this.key = key;
        this.encryptData = encryptData;
    }
}
