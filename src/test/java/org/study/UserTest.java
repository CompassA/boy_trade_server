package org.study;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.UserMapper;
import org.study.dao.UserPasswordMapper;
import org.study.data.UserDO;
import org.study.data.UserPasswordDO;
import org.study.service.model.UserModel;
import org.study.util.DataToModelUtil;

import java.util.Objects;
import java.util.Optional;

/**
 * @author fanqie
 * @date 2019/12/8
 */
public class UserTest extends BaseTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserPasswordMapper userPasswordMapper;

    @Test
    @Transactional
    @Rollback
    public void userMapperTest() {
        //插入用户
        final UserDO userDO = new UserDO()
                .setAccount("test")
                .setName("test");
        Assert.assertEquals(1, userMapper.upsertUser(userDO));
        Assert.assertTrue(Objects.nonNull(userDO.getUserId()));

        //插入密码
        final UserPasswordDO passwordDO = new UserPasswordDO()
                .setPassword("xxxxx")
                .setUserId(userDO.getUserId());
        Assert.assertEquals(1, userPasswordMapper.insertPassword(passwordDO));

        //查询用户
        final UserDO user = userMapper.selectByAccount(userDO.getAccount());
        final UserPasswordDO password = userPasswordMapper.selectPasswordById(user.getUserId());
        final Optional<UserModel> userModel = DataToModelUtil.getUserModel(user, password);

        Assert.assertTrue(userModel.isPresent());
        System.out.println("用户领域模型： " + userModel);
    }
}
