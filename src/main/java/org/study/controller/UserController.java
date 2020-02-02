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
import org.study.service.RedisService;
import org.study.service.SessionService;
import org.study.service.UserService;
import org.study.util.ModelToViewUtil;
import org.study.view.LoginDTO;
import org.study.view.RegistryDTO;
import org.study.view.UserVO;

import java.util.Optional;

/**
 * @author fanqie
 * @date 2019/12/8
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private RedisService redisService;

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
        final Optional<UserVO> userOpt = ModelToViewUtil.getUserVO(userModel.get());
        if (userOpt.isPresent()) {
            final String token = sessionService.putUserModel(userModel.get());
            final UserVO userView = userOpt.get();
            userView.setToken(token);
            return ServerResponse.create(userView);
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
        final Optional<UserVO> userOpt = ModelToViewUtil.getUserVO(userModel);
        if (userOpt.isPresent()) {
            final String token = sessionService.putUserModel(userModel);
            final UserVO userView = userOpt.get();
            userView.setToken(token);
            return ServerResponse.create(userView);
        }
        throw new ServerException(ServerExceptionBean.USER_REGISTRY_EXCEPTION);
    }

    @GetMapping(value = ApiPath.User.EXIST)
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
        //获取缓存
        final String key = this.generateUserKey(userId);
        final Optional<UserVO> cache = redisService.getCache(key, UserVO.class);
        if (cache.isPresent()) {
            return ServerResponse.create(cache.get());
        }

        //查询数据库
        final Optional<UserVO> userInfo = userService.queryByPrimaryKey(userId);
        if (userInfo.isPresent()) {
            redisService.cacheData(key, userInfo.get());
            return ServerResponse.create(userInfo.get());
        }
        throw new ServerException(ServerExceptionBean.USER_QUERY_EXCEPTION);
    }

    @GetMapping(value = ApiPath.User.SESSION_CHECKING)
    public ServerResponse getSessionStatus(@RequestParam("token") final String token) {
        return sessionService.getUserModel(token)
                .map(ServerResponse::create)
                .orElse(ServerResponse.create(null));
    }

    @GetMapping(value = ApiPath.User.LOGOUT)
    public ServerResponse logout(@RequestParam("token") final String token) {
        sessionService.logout(token);
        return ServerResponse.create(null);
    }

    private String generateUserKey(final Integer userId) {
        return String.format("user:%d", userId);
    }
}