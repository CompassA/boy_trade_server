package org.study.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.model.UserModel;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author fanqie
 * @date 2020/1/19
 */
@Service
public class FileServiceImpl {

    @Autowired
    private FileServiceConfig config;

    /**
     * 上传文件服务
     * @param file 上传的文件
     * @param user 登录用户的信息
     * @return 文件的url
     */
    public String uploadFile(
            final MultipartFile file, final UserModel user) throws ServerException {
        //文件名 = userId + 全局唯一UUID
        final String fileName = user.getUserId().toString() + UUID.randomUUID().toString();
        final File des = new File(config.getImageDictionary() + File.separator + fileName);
        if (!des.getParentFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            des.mkdirs();
        }

        //将文件保存到本地
        try {
            file.transferTo(des);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new ServerException(ServerExceptionBean.FILE_EXCEPTION);
        }

        //返回url
        return "Hello world";
    }
}
