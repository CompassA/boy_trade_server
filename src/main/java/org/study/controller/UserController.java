package org.study.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.study.controller.response.ServerRequest;
import org.study.controller.response.ServerResponse;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.EncryptService;
import org.study.service.RedisService;
import org.study.service.SessionService;
import org.study.service.UserService;
import org.study.service.model.UserModel;
import org.study.service.model.enumdata.CacheType;
import org.study.util.ModelToViewUtil;
import org.study.util.MyStringUtil;
import org.study.view.LoginDTO;
import org.study.view.RegistryDTO;
import org.study.view.UserVO;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author fanqie
 * Created on 2019/12/8
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private SessionService sessionService;

    @Resource
    private EncryptService encryptService;

    @Resource
    private RedisService redisService;

    @Resource
    private FileController fileController;

    @PostMapping(value = ApiPath.User.LOGIN)
    public ServerResponse login(@RequestBody ServerRequest serverRequest) throws ServerException {
        //反序列化判空
        final LoginDTO loginDTO = encryptService.deserialize(serverRequest, LoginDTO.class);
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
    public ServerResponse registry(@RequestBody ServerRequest data) throws ServerException {
        //反序列化
        final RegistryDTO registryDTO = encryptService.deserialize(data, RegistryDTO.class);

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
    public ServerResponse checkUserName(@RequestParam("name") String name) throws ServerException {
        if (StringUtils.isBlank(name)) {
            throw new ServerException(ServerExceptionBean.PARAMETER_VALIDATION_EXCEPTION);
        }
        return ServerResponse.create(userService.isUserNameExists(name));
    }

    @GetMapping(value = ApiPath.User.PART_INFO)
    public ServerResponse selectUserInfo(@RequestParam("userId") final Integer userId)
            throws ServerException {
        //获取缓存
        final String key = MyStringUtil.getCacheKey(userId, CacheType.USER_INFO);
        final Optional<UserVO> cache = redisService.getCache(key);
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

    @PostMapping(value = ApiPath.User.UPDATE_ICON)
    public ServerResponse uploadIconUrl(
            @RequestParam("userId") Integer userId, @RequestParam("token") String token,
            @RequestPart("imgFile") final MultipartFile file) throws ServerException {
        final ServerResponse response = fileController.uploadFile(userId, token, file);
        if (!userService.updateIconUrl(userId, (String) response.getBody())) {
            throw new ServerException(ServerExceptionBean.USER_ICON_URL_LOAD_FAIL_EXCEPTION);
        }
        return response;
    }
}