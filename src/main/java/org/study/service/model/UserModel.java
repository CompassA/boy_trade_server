package org.study.service.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author fanqie
 * Created on 2020/1/4
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class UserModel implements Serializable {

    private Integer userId;

    private String account;

    @NotBlank(message = "用户名不能为空")
    private String name;

    private String iconUrl;

    @NotBlank(message = "密码不能为空")
    private String password;

    private Timestamp createTime;

    private Timestamp updateTime;
}
