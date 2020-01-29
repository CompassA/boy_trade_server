package org.study.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.model.UserModel;
import org.study.service.SessionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/1/29
 */
@Service
public class SessionServiceImpl implements SessionService {

    protected static final String USER_MODEL = "userModel";

    protected static final String LOGIN_MARK = "isLogin";

    @Autowired
    private HttpServletRequest servletRequest;

    @Override
    public boolean isLogin() {
        return servletRequest.getSession().getAttribute(LOGIN_MARK).equals(true);
    }

    @Override
    public Optional<UserModel> getUserModel() {
        final Object userModel = servletRequest.getSession().getAttribute(USER_MODEL);
        if (userModel == null) {
            return Optional.empty();
        }
        return Optional.of((UserModel) userModel);
    }

    @Override
    public void putUserModel(final UserModel userModel) {
        final HttpSession session = servletRequest.getSession();
        session.setAttribute(LOGIN_MARK, true);
        session.setAttribute(USER_MODEL, userModel);
    }

    @Override
    public void logout() {
        final HttpSession session = servletRequest.getSession();
        session.setAttribute(LOGIN_MARK, false);
        session.setAttribute(USER_MODEL, null);
    }
}
