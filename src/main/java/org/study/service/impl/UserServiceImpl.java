package org.study.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.UserMapper;
import org.study.dao.UserPasswordMapper;
import org.study.data.UserDO;
import org.study.data.UserPasswordDO;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.model.UserModel;
import org.study.service.UserService;
import org.study.util.DataToModelUtil;
import org.study.util.ModelToDataUtil;
import org.study.validation.ValidationResult;
import org.study.validation.ValidatorImpl;

import java.util.Objects;
import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/1/4
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserPasswordMapper userPasswordMapper;

    @Autowired
    private ValidatorImpl validator;

    /**
     * 登录校验
     * @param account 账号
     * @param password 密码
     * @return 用户领域模型
     */
    @Override
    public Optional<UserModel> login(final String account, final String password) {
        //用户数据
        final UserDO userDO = userMapper.selectByAccount(account);
        if (Objects.isNull(userDO)) {
            return Optional.empty();
        }

        //密码数据
        final UserPasswordDO userPasswordDO = userPasswordMapper
                .selectPasswordById(userDO.getUserId());

        //校验密码
        if (Objects.isNull(userPasswordDO) ||
                !password.equals(userPasswordDO.getPassword())) {
            return Optional.empty();
        }

        return DataToModelUtil.getUserModel(userDO, userPasswordDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserModel registry(final UserModel userModel)
            throws ServerException {
        //入参校验
        final ValidationResult result = validator.validate(userModel);
        if (result.hasErrors()) {
            throw new ServerException(
                    ServerExceptionBean.USER_REGISTRY_EXCEPTION,
                    result.getErrorMsg());
        }
        final Optional<UserDO> userDO = ModelToDataUtil.getUserDO(userModel);
        final Optional<UserPasswordDO> userPasswordDO = ModelToDataUtil.getUserPasswordDO(userModel);
        if (!userDO.isPresent() || !userPasswordDO.isPresent()) {
            throw new ServerException(ServerExceptionBean.USER_REGISTRY_EXCEPTION);
        }

        //信息入库
        final UserDO userData = userDO.get();
        userMapper.upsertUser(userData);
        final Integer insertedUserId = userData.getUserId();

        final UserPasswordDO passwordData = userPasswordDO.get();
        passwordData.setUserId(insertedUserId);
        userPasswordMapper.insertPassword(passwordData);

        //返回数据库状态
        final Optional<UserModel> userStatus = DataToModelUtil.getUserModel(
                userMapper.selectByAccount(userData.getAccount()),
                userPasswordMapper.selectPasswordById(insertedUserId));
        if (userStatus.isPresent()) {
            return userStatus.get();
        }
        throw new ServerException(ServerExceptionBean.USER_REGISTRY_EXCEPTION);
    }
}
