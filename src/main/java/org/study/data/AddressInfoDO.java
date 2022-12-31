package org.study.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @author fanqie
 * Created on 2020/2/7
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class AddressInfoDO {

    private Integer infoId;

    private Integer userId;

    private String userName;

    private String userPhone;

    private String userAddress;

    private Byte gender;

    private Byte selected;

    private Timestamp createTime;

    private Timestamp updateTime;
}
