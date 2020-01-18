package org.study.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

/**
 * @author fanqie
 * @date 2020/1/4
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserModel {

    private Integer userId;

    private String account;

    @NotBlank(message = "用户名不能为空")
    private String name;

    private String iconUrl;

    @NotBlank(message = "密码不能为空")
    private String password;

    private Timestamp createTime;

    private Timestamp updateTime;

    @Override
    public String toString() {
        return "UserModel{" +
                "userId=" + userId +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
