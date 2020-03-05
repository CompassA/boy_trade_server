package org.study.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.study.data.AddressInfoDO;

import java.util.List;

/**
 * @author fanqie
 * @date 2020/2/7
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class AddressInfoModel {

    private Integer userId;

    private List<AddressInfoDO> userAddressInfo;
}
