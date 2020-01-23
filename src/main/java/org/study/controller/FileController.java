package org.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.model.UserModel;
import org.study.response.ServerResponse;
import org.study.service.FileService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/1/19
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class FileController extends BaseController {

    private static final int maxSize = 3 * 1024 * 1024;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private FileService fileService;

    @PostMapping(value = ApiPath.LoadingFile.UPLOAD)
    public ServerResponse uploadFile(
            @RequestPart("imgFile") final MultipartFile file) throws ServerException {
        //登录校验
        if (!this.isLogin(httpServletRequest) || file.getSize() > maxSize) {
            throw new ServerException(ServerExceptionBean.FILE_EXCEPTION);
        }
        final Optional<UserModel> user = this.getUserModel(httpServletRequest);
        if (!user.isPresent()) {
            throw new ServerException(ServerExceptionBean.FILE_EXCEPTION);
        }
        //上传文件并返回url
        final String url = fileService.uploadFile(file, user.get());
        return ServerResponse.create(url);
    }
}
