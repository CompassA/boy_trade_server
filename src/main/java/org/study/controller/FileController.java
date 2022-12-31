package org.study.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.study.controller.response.ServerResponse;
import org.study.error.ServerException;
import org.study.error.ServerExceptionEnum;
import org.study.service.FileService;
import org.study.service.SessionService;
import org.study.service.model.UserModel;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author fanqie
 * Created on 2020/1/19
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class FileController {

    private static final int MAX_FILE_SIZE = 3 * 1024 * 1024;

    @Resource
    private SessionService sessionService;

    @Resource
    private FileService fileService;

    @PostMapping(value = ApiPath.LoadingFile.UPLOAD)
    public ServerResponse uploadFile(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestPart("imgFile") final MultipartFile file) throws ServerException {
        //登录校验
        if (!sessionService.isLogin(token, userId) || file.getSize() > MAX_FILE_SIZE) {
            throw new ServerException(ServerExceptionEnum.FILE_EXCEPTION);
        }
        final Optional<UserModel> user = sessionService.getUserModel(token);
        if (!user.isPresent()) {
            throw new ServerException(ServerExceptionEnum.FILE_EXCEPTION);
        }
        //上传文件并返回url
        final String url = fileService.uploadFile(file, user.get());
        return ServerResponse.create(url);
    }
}
