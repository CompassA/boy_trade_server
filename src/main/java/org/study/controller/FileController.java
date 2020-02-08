package org.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.model.UserModel;
import org.study.controller.response.ServerResponse;
import org.study.service.FileService;
import org.study.service.SessionService;

import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/1/19
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class FileController {

    private static final int MAX_FILE_SIZE = 3 * 1024 * 1024;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private FileService fileService;

    @PostMapping(value = ApiPath.LoadingFile.UPLOAD)
    public ServerResponse uploadFile(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestPart("imgFile") final MultipartFile file) throws ServerException {
        //登录校验
        if (!sessionService.isLogin(token, userId) || file.getSize() > MAX_FILE_SIZE) {
            throw new ServerException(ServerExceptionBean.FILE_EXCEPTION);
        }
        final Optional<UserModel> user = sessionService.getUserModel(token);
        if (!user.isPresent()) {
            throw new ServerException(ServerExceptionBean.FILE_EXCEPTION);
        }
        //上传文件并返回url
        final String url = fileService.uploadFile(file, user.get());
        return ServerResponse.create(url);
    }
}
