package org.study;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.UserDao;
import org.study.dao.UserPasswordDao;
import org.study.data.UserDO;
import org.study.data.UserPasswordDO;
import org.study.model.UserModel;
import org.study.util.DataToModelUtil;
import org.study.util.ModelToViewUtil;
import org.study.view.UserVO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author fanqie
 * @date 2019/12/8
 */
public class UserTest extends BaseTest {

    @Autowired
    private UserDao userDAO;

    @Autowired
    private UserPasswordDao userPasswordDao;

    @Test
    @Transactional
    public void userMapperTest() {
        final UserDO user = new UserDO();
        user.setName("哈哈哈");
        user.setAccount("123456");
        userDAO.upsertUser(user);
        final UserDO selectUser = userDAO.selectUser(user).iterator().next();
        System.out.println(selectUser);
        Assert.assertTrue(Objects.nonNull(selectUser));
    }

    @Test
    public void userPasswordSelectTest() {
        final UserDO user = new UserDO()
                .setUserId(3)
                .setName("番茄")
                .setAccount("admin");

        final List<UserDO> selectUserDOResult = userDAO.selectUser(user);
        Assert.assertEquals(1, selectUserDOResult.size());

        final UserPasswordDO selectUserPasswordResult =
                userPasswordDao.selectPasswordById(user.getUserId());

        final Optional<UserModel> userModel = DataToModelUtil
                .getUserModel(selectUserDOResult.get(0), selectUserPasswordResult);
        Assert.assertTrue(userModel.isPresent());
        System.out.println(userModel.get().toString());

        final Optional<UserVO> userVO = ModelToViewUtil.getUserVO(userModel.get());
        Assert.assertTrue(userVO.isPresent());
        System.out.println(userVO.get().toString());
    }
}
