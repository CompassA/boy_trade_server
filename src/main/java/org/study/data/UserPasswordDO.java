package org.study.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @author fanqie
 * @date 2020/1/4
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class UserPasswordDO {

    private Integer userId;

    private String password;

    private Timestamp createTime;

    private Timestamp updateTime;
}
