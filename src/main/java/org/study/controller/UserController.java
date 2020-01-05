package org.study.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.study.error.ServerException;
import org.study.error.SystemExceptionBean;
import org.study.model.UserModel;
import org.study.response.ServerResponse;
import org.study.service.api.UserServiceApi;
import org.study.util.ModelToViewUtil;
import org.study.view.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * @author fanqie
 * @date 2019/12/8
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController {

    @Autowired
    private UserServiceApi userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping(value = ApiPath.LOGIN, consumes={CONSUMERS})
    public ServerResponse userLogin(
            @RequestParam(ACCOUNT) final String account,
            @RequestParam(PASSWORD) final String password)
            throws ServerException {
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            throw new ServerException(
                    SystemExceptionBean.PARAMETER_VALIDATION_EXCEPTION);
        }

        final Optional<UserModel> userModel =
                userService.login(account, password);
        if (!userModel.isPresent()) {
            throw new ServerException(SystemExceptionBean.USER_LOGIN_EXCEPTION);
        }

        final Optional<UserVO> userVO = ModelToViewUtil.getUserVO(userModel.get());
        if (userVO.isPresent()) {
            final HttpSession session = httpServletRequest.getSession();
            session.setAttribute(LOGIN_MARK, true);
            session.setAttribute(USER_MODEL, userModel);
            return ServerResponse.create(userVO.get());
        } else {
            throw new ServerException(SystemExceptionBean.USER_LOGIN_EXCEPTION);
        }
    }
}
