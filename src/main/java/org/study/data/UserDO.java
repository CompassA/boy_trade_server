package org.study.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @author fanqie
 * @date 2019/12/8
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserDO {

    private Integer userId;

    private String account;

    private String name;

    private String iconUrl;

    private Timestamp createTime;

    private Timestamp updateTime;

    @Override
    public String toString() {
        return "UserDO{" +
                "userId=" + userId +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
