package org.study;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.UserMapper;
import org.study.dao.UserPasswordMapper;
import org.study.data.UserDO;
import org.study.data.UserPasswordDO;
import org.study.service.model.UserModel;
import org.study.test.config.SpringMockTestBase;
import org.study.util.DataToModelUtil;

import java.util.Objects;
import java.util.Optional;

/**
 * @author fanqie
 * Created on 2019/12/8
 */
public class UserTest extends SpringMockTestBase {

    private final UserMapper userMapper;
    private final UserPasswordMapper userPasswordMapper;

    public UserTest() {
        super();
        this.userMapper = this.context.getBean(UserMapper.class);
        this.userPasswordMapper = this.context.getBean(UserPasswordMapper.class);
    }

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
