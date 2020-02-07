package org.study.service.model;

import lombok.Getter;
import lombok.Setter;
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
public class AddressInfoModel {

    private Integer userId;

    private List<AddressInfoDO> userAddressInfo;
}
