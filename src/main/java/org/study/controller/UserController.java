package org.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.study.dao.UserDao;
import org.study.data.UserDO;

/**
 * @author fanqie
 * @date 2019/12/8
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserDao userDao;

    @GetMapping("/info")
    public UserDO getUserInfo(@RequestParam("id") int id) {
        final UserDO userDO = new UserDO();
        return userDao.selectUser(userDO)
                .iterator()
                .next();
    }
}
