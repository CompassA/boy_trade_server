package org.study.service;

import org.springframework.web.multipart.MultipartFile;
import org.study.error.ServerException;
import org.study.model.UserModel;

/**
 * @author fanqie
 * @date 2020/1/23
 */
public interface FileService {

    /**
     * 上传文件服务
     * @param file 上传的文件
     * @param user 登录用户的信息
     * @return 文件的url
     * @throws ServerException 文件上传失败
     */
    String uploadFile(final MultipartFile file, final UserModel user) throws ServerException;
}
