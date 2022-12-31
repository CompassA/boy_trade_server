package org.study.service;

import org.study.data.AddressInfoDO;
import org.study.error.ServerException;
import org.study.service.model.AddressInfoModel;

import java.util.Optional;

/**
 * @author fanqie
 * Created on 2020/2/7
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

    /**
     * 重新设置用户的默认地址信息
     * @param userId 用户id
     * @param targetInfoId 要置为默认的地址信息id
     * @return 重置后的用户地址信息列表
     * @throws ServerException 更新的SQL语句执行错误
     */
    AddressInfoModel resetDefaultInfo(final Integer userId, final Integer targetInfoId)
            throws ServerException;

    /**
     * 查询用户的默认地址信息
     * @param userId 目标用户id
     * @return 默认地址信息
     */
    Optional<AddressInfoDO> getDefaultInfo(final Integer userId);
}
