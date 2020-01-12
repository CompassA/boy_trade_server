package org.study.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.dao.UserMapper;
import org.study.dao.UserPasswordMapper;
import org.study.data.UserDO;
import org.study.data.UserPasswordDO;
import org.study.model.UserModel;
import org.study.service.UserService;
import org.study.util.DataToModelUtil;

import java.util.Objects;
import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/1/4
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userDao;

    @Autowired
    private UserPasswordMapper userPasswordMapper;

    /**
     * 登录校验
     * @param account 账号
     * @param password 密码
     * @return 用户领域模型
     */
    @Override
    public Optional<UserModel> login(final String account, final String password) {
        //用户数据
        final UserDO userDO = userDao.selectByAccount(account);
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
}
