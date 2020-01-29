package org.study.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.model.UserModel;
import org.study.response.ServerRequest;
import org.study.response.ServerResponse;
import org.study.service.EncryptService;
import org.study.service.UserService;
import org.study.util.ModelToViewUtil;
import org.study.view.LoginDTO;
import org.study.view.RegistryDTO;
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
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private EncryptService encryptService;

    @PostMapping(value = ApiPath.User.LOGIN)
    public ServerResponse login(
            @RequestBody final ServerRequest serverRequest) throws ServerException {
        final LoginDTO loginDTO;
        try {
            loginDTO = serverRequest.deserialize(encryptService, LoginDTO.class);
        } catch (final Exception ex) {
            throw new ServerException(ServerExceptionBean.ENCRYPT_DECRYPT_EXCEPTION);
        }

        if (StringUtils.isBlank(loginDTO.getAccount())
                || StringUtils.isBlank(loginDTO.getPassword())) {
            throw new ServerException(ServerExceptionBean.PARAMETER_VALIDATION_EXCEPTION);
        }
        final Optional<UserModel> userModel = userService.login(
                loginDTO.getAccount(), encryptService.encryptByMd5(loginDTO.getPassword()));
        if (!userModel.isPresent()) {
            throw new ServerException(ServerExceptionBean.USER_LOGIN_EXCEPTION);
        }
        final Optional<UserVO> userVO = ModelToViewUtil.getUserVO(userModel.get());
        if (userVO.isPresent()) {
            final HttpSession session = httpServletRequest.getSession();
            session.setAttribute(LOGIN_MARK, true);
            session.setAttribute(USER_MODEL, userModel.get());
            return ServerResponse.create(userVO.get());
        }
        throw new ServerException(ServerExceptionBean.USER_LOGIN_EXCEPTION);
    }

    @PostMapping(value = ApiPath.User.REGISTRY)
    public ServerResponse registry(
            @RequestBody final ServerRequest data) throws ServerException {
        final RegistryDTO registryDTO;
        try {
            registryDTO = data.deserialize(encryptService, RegistryDTO.class);
        } catch (final Exception ex) {
            throw new ServerException(ServerExceptionBean.ENCRYPT_DECRYPT_EXCEPTION);
        }
        final UserModel registryModel = new UserModel()
                .setName(registryDTO.getName())
                .setPassword(encryptService.encryptByMd5(registryDTO.getPassword()));
        final UserModel userModel = userService.registry(registryModel);
        final Optional<UserVO> userVO = ModelToViewUtil.getUserVO(userModel);
        if (userVO.isPresent()) {
            final HttpSession session = httpServletRequest.getSession();
            session.setAttribute(LOGIN_MARK, true);
            session.setAttribute(USER_MODEL, userModel);
            return ServerResponse.create(userVO.get());
        }
        throw new ServerException(ServerExceptionBean.USER_REGISTRY_EXCEPTION);
    }

    @GetMapping(value = ApiPath.User.EXIST, consumes = { CONSUMERS })
    public ServerResponse checkUserName(
            @RequestParam("name") final String name) throws ServerException {
        if (StringUtils.isBlank(name)) {
            throw new ServerException(ServerExceptionBean.PARAMETER_VALIDATION_EXCEPTION);
        }
        return ServerResponse.create(userService.isUserNameExists(name));
    }

    @GetMapping(value = ApiPath.User.PART_INFO)
    public ServerResponse selectUserInfo(
            @RequestParam("userId") final Integer userId) throws ServerException {
        final Optional<UserVO> userInfo = userService.queryByPrimaryKey(userId);
        if (userInfo.isPresent()) {
            return ServerResponse.create(userInfo.get());
        }
        throw new ServerException(ServerExceptionBean.USER_QUERY_EXCEPTION);
    }
}
