package org.study.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @author fanqie
 * Created on 2019/12/8
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class UserDO {

    private Integer userId;

    private String account;

    private String name;

    private String iconUrl;

    private Timestamp createTime;

    private Timestamp updateTime;
}
