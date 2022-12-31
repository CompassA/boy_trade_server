package org.study.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.AddressInfoMapper;
import org.study.data.AddressInfoDO;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.AddressInfoService;
import org.study.service.model.AddressInfoModel;
import org.study.util.DataToModelUtil;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author fanqie
 * Created on 2020/2/7
 */
@Service
public class AddressInfoServerImpl implements AddressInfoService {

    @Resource
    private AddressInfoMapper infoMapper;

    @Override
    public AddressInfoModel getUserInfoModel(final Integer userId) {
        return DataToModelUtil.getAddressInfoModel(userId, infoMapper.selectByUserId(userId));
    }

    @Override
    public AddressInfoModel addNewInfo(final Integer userId, final AddressInfoDO info)
            throws ServerException {
        if (this.isPhoneNotValid(info) || infoMapper.insertOrUpdate(info) < 1) {
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
        if (this.isPhoneNotValid(info) || infoMapper.insertOrUpdate(info) < 1) {
            throw new ServerException(ServerExceptionBean.ADDRESS_INFO_UPDATE_EXCEPTION);
        }
        return DataToModelUtil.getAddressInfoModel(userId, infoMapper.selectByUserId(userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddressInfoModel resetDefaultInfo(final Integer userId, final Integer targetInfoId)
            throws ServerException {
        infoMapper.cancelSelected(userId);
        if (infoMapper.updateSelected(targetInfoId, userId) < 1) {
            throw new ServerException(ServerExceptionBean.ADDRESS_SELECTED_RESET_EXCEPTION);
        }
        return DataToModelUtil.getAddressInfoModel(userId, infoMapper.selectByUserId(userId));
    }

    @Override
    public Optional<AddressInfoDO> getDefaultInfo(final Integer userId) {
        return Optional.ofNullable(infoMapper.selectDefault(userId));
    }

    private boolean isPhoneNotValid(final AddressInfoDO info) {
        try {
            final String originPhone = info.getUserPhone();
            final Long phoneAfterLongParse = Long.parseLong(originPhone);
            final String phoneAfterStrParse = String.valueOf(phoneAfterLongParse);
            return originPhone.length() != 11 || !originPhone.equals(phoneAfterStrParse);
        } catch (final Exception e) {
            return true;
        }
    }
}
