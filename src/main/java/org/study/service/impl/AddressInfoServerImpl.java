package org.study.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.dao.AddressInfoMapper;
import org.study.data.AddressInfoDO;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.AddressInfoService;
import org.study.service.model.AddressInfoModel;
import org.study.util.DataToModelUtil;

/**
 * @author fanqie
 * @date 2020/2/7
 */
@Service
public class AddressInfoServerImpl implements AddressInfoService {

    @Autowired
    private AddressInfoMapper infoMapper;

    @Override
    public AddressInfoModel getUserInfoModel(final Integer userId) {
        return DataToModelUtil.getAddressInfoModel(userId, infoMapper.selectByUserId(userId));
    }

    @Override
    public AddressInfoModel addNewInfo(final Integer userId, final AddressInfoDO info)
            throws ServerException {
        if (infoMapper.insertOrUpdate(info) < 1) {
            throw new ServerException(ServerExceptionBean.ADDRESS_INFO_INSERT_EXCEPTION);
        }
        return DataToModelUtil.getAddressInfoModel(userId, infoMapper.selectByUserId(userId));
    }

    @Override
    public AddressInfoModel deleteInfo(final Integer userId, final Integer infoId)
            throws ServerException {
        if (infoMapper.deleteById(infoId, userId) < 1) {
            throw new ServerException(ServerExceptionBean.ADDRESS_INFO_DELETE_EXCEPTION);
        }
        return DataToModelUtil.getAddressInfoModel(userId, infoMapper.selectByUserId(userId));
    }

    @Override
    public AddressInfoModel updateInfo(final Integer userId, final AddressInfoDO info)
            throws ServerException {
        if (infoMapper.insertOrUpdate(info) < 1) {
            throw new ServerException(ServerExceptionBean.ADDRESS_INFO_UPDATE_EXCEPTION);
        }
        return DataToModelUtil.getAddressInfoModel(userId, infoMapper.selectByUserId(userId));
    }
}
