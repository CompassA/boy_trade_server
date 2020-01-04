package org.study.data;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author fanqie
 * @date 2020/1/4
 */
@Getter
@Setter
public class UserPasswordDO {

    private Integer userId;

    private String password;

    private Timestamp createTime;

    private Timestamp updateTime;

    @Override
    public String toString() {
        return "UserPasswordDO{" +
                "userId=" + userId +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
