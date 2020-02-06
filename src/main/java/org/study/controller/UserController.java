package org.study.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.study.controller.response.ServerRequest;
import org.study.controller.response.ServerResponse;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.EncryptService;
import org.study.service.RedisService;
import org.study.service.SessionService;
import org.study.service.UserService;
import org.study.service.model.CacheType;
import org.study.service.model.UserModel;
import org.study.util.ModelToViewUtil;
import org.study.util.MyStringUtil;
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
    public ServerResponse login(@RequestBody final ServerRequest serverRequest)
            throws ServerException {
        //反序列化判空
        final LoginDTO loginDTO = serverRequest.deserialize(encryptService, LoginDTO.class);
        final String account = loginDTO.getAccount();
        final String password = loginDTO.getPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            throw new ServerException(ServerExceptionBean.PARAMETER_VALIDATION_EXCEPTION);
        }
        final String encryptPassword = encryptService.encryptByMd5(password);

        //登录并返回前端
        return userService.login(account, encryptPassword)
                .flatMap(userModel -> ModelToViewUtil.getUserVO(userModel)
                        .map(userVO -> {
                            final String token = sessionService.putUserModel(userModel);
                            userVO.setToken(token);
                            return userVO;
                        }))
                .map(ServerResponse::create)
                .orElse(ServerResponse.fail(ServerExceptionBean.USER_LOGIN_EXCEPTION));
    }

    @PostMapping(value = ApiPath.User.REGISTRY)
    public ServerResponse registry(@RequestBody final ServerRequest data) throws ServerException {
        //反序列化
        final RegistryDTO registryDTO = data.deserialize(encryptService, RegistryDTO.class);

        //插入用户注册数据
        final UserModel registryModel = new UserModel()
                .setName(registryDTO.getName())
                .setPassword(encryptService.encryptByMd5(registryDTO.getPassword()));
        final UserModel userModel = userService.registry(registryModel);

        //保存session，进入登录态，并返沪前端
        return ModelToViewUtil.getUserVO(userModel)
                .map(userVO -> {
                    final String token = sessionService.putUserModel(userModel);
                    userVO.setToken(token);
                    return ServerResponse.create(userVO);
                }).orElse(ServerResponse.fail(ServerExceptionBean.USER_REGISTRY_EXCEPTION));
    }

    @GetMapping(value = ApiPath.User.EXIST)
    public ServerResponse checkUserName(@RequestParam("name") final String name)
            throws ServerException {
        if (StringUtils.isBlank(name)) {
            throw new ServerException(ServerExceptionBean.PARAMETER_VALIDATION_EXCEPTION);
        }
        return ServerResponse.create(userService.isUserNameExists(name));
    }

    @GetMapping(value = ApiPath.User.PART_INFO)
    public ServerResponse selectUserInfo(@RequestParam("userId") final Integer userId)
            throws ServerException {
        //获取缓存
        final String key = MyStringUtil.generateCacheKey(userId, CacheType.USER_INFO);
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
}