package org.study;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.study.dao.UserDao;
import org.study.data.UserDO;

import java.util.Objects;

/**
 * @author fanqie
 * @date 2019/12/8
 */
public class UserTest extends BaseTest {

    @Autowired
    private UserDao userDAO;

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
}
