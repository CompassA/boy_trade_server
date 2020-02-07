package org.study.service;

import org.study.controller.ApiPath;
import org.study.data.AddressInfoDO;
import org.study.error.ServerException;
import org.study.service.model.AddressInfoModel;

/**
 * @author fanqie
 * @date 2020/2/7
 */
public interface AddressInfoService {

    /**
     * 根据用户id查询用户填写的地址信息
     * @param userId 用户id
     * @return 地址信息
     */
    AddressInfoModel getUserInfoModel(final Integer userId);

    /**
     * 新增地址信息
     * @param userId 要新增的用户
     * @param info 新增的地址信息
     * @return 新增后用户的所有地址信息
     * @throws ServerException 插入失败
     */
    AddressInfoModel addNewInfo(final Integer userId, final AddressInfoDO info)
            throws ServerException;

    /**
     * 根据地址信息id删除信息下
     * @param userId 用户id, 用于用户信息校验
     * @param infoId 地址id
     * @return 删除后用户的所有地址信息
     * @throws ServerException 删除失败
     */
    AddressInfoModel deleteInfo(final Integer userId, final Integer infoId) throws ServerException;

    /**
     * 更新地址信息
     * @param userId 用户id, 用于用户信息校验
     * @param info 地址id
     * @return 更新后用户的所有地址信息
     * @throws ServerException 更新失败
     */
    AddressInfoModel updateInfo(final Integer userId, final AddressInfoDO info)
            throws ServerException;
}
