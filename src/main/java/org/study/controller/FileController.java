package org.study.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.study.aspects.annotation.EnableTokenValidation;
import org.study.aspects.annotation.UserInfoContext;
import org.study.controller.response.ServerResponse;
import org.study.error.ServerException;
import org.study.error.ServerExceptionEnum;
import org.study.service.FileService;

import javax.annotation.Resource;

/**
 * @author fanqie
 * Created on 2020/1/19
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class FileController {

    private static final int MAX_FILE_SIZE = 3 * 1024 * 1024;

    @Resource
    private FileService fileService;

    @EnableTokenValidation
    @PostMapping(value = ApiPath.LoadingFile.UPLOAD)
    public ServerResponse uploadFile(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestPart("imgFile") final MultipartFile file) throws ServerException {
        //登录校验
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ServerException(ServerExceptionEnum.FILE_EXCEPTION);
        }
        //上传文件并返回url
        final String url = fileService.uploadFile(file, UserInfoContext.get());
        return ServerResponse.create(url);
    }
}
