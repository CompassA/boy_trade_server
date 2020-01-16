package org.study.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author fanqie
 * @date 2020/1/16
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginDTO {

    private String account;

    private String password;

    @Override
    public String toString() {
        return "LoginDTO{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
