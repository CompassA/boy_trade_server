package org.study.service.impl;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.study.config.FileServiceConfig;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.model.UserModel;
import org.study.service.FileService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * ftp图片主目录 /image/
 * nginx代理这个资源
 * @author fanqie
 * @date 2020/1/19
 */
@Service
public class FileServiceImpl implements FileService {

    private static final String JPG_SUFFIX = ".jpg";

    private static final String PNG_SUFFIX = ".png";

    @Autowired
    private FileServiceConfig config;

    @Override
    @Deprecated
    public String uploadFile(
            final MultipartFile file, final UserModel user) throws ServerException {
        final String fileName = this.generateFileName(file, user);
        final File des = new File(config.getLocalImageDictionary() + fileName);
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

        //将文件上传至ftp，删除本地文件，并返回url
        final String ftpPath = this.generateFtpPath(user.getUserId());
        final String resourceUrl = this.uploadFtp(des, fileName, ftpPath);
        //noinspection ResultOfMethodCallIgnored
        des.delete();
        return resourceUrl;
    }

    @Override
    public String uploadFileToLocal(MultipartFile file, UserModel user) throws ServerException {
        //file name
        final String fileName = generateFileName(file, user);

        //path after nginx url
        final String serverPath = user.getUserId() + File.separator + fileName;

        //local path
        final File des = new File(config.getLocalImageDictionary() + serverPath);

        //create parent dictionaries
        if (!des.getParentFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            des.mkdirs();
        }

        //save file
        try {
            file.transferTo(des);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerException(ServerExceptionBean.FILE_EXCEPTION);
        }

        //generate url
        return config.getNginxResourceUrl() + serverPath;
    }


    /**
     * 将文件上传到ftp
     * @param file 要上传的文件
     * @param fileName 文件名
     * @param ftpPath ftp路径
     * @return url
     * @throws ServerException 上传失败
     */
    private String uploadFtp(
            final File file, final String fileName, final String ftpPath) throws ServerException {
        final FTPClient client = new FTPClient();
        InputStream fis = null;
        try {
            //连接ftp
            client.connect(config.getFtpHost(), config.getFtpPort());
            client.login(config.getFtpUser(), config.getFtpPassword());
            client.setDataTimeout(config.getFtpTimeout());
            if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
                client.disconnect();
                throw new ServerException(ServerExceptionBean.FILE_EXCEPTION);
            }

            //FTP设置成被动模式
            fis = new FileInputStream(file);
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();

            //创建用户空间
            if(!client.changeWorkingDirectory(ftpPath)){
                client.makeDirectory(ftpPath);
            }
            client.changeWorkingDirectory(ftpPath);
            final String remoteFilePath = ftpPath + fileName;
            if (!client.storeFile(remoteFilePath, fis)) {
                throw new ServerException(ServerExceptionBean.FILE_EXCEPTION);
            }

            //返回url
            return this.generateImgUrl(remoteFilePath);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new ServerException(ServerExceptionBean.FILE_EXCEPTION);
        } finally {
            IOUtils.closeQuietly(fis);
            try {
                client.logout();
                client.disconnect();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成文件名
     * @param userModel 用户信息
     * @return 文件名 = userId + 全局唯一UUID
     */
    private String generateFileName(
            final MultipartFile file, final UserModel userModel) throws ServerException {
        final String suffix = this.getSuffix(file.getOriginalFilename());
        return userModel.getUserId().toString() + UUID.randomUUID().toString() + suffix;
    }

    /**
     * 生成图片url
     * @param ftpResourcePath 图片文件路径
     * @return url = nginx代理 + ftp资源路径 http://123.456.789.000:1234/image/imageName
     */
    private String generateImgUrl(final String ftpResourcePath) {
        return config.getNginxResourceUrl() + ftpResourcePath;
    }

    /**
     * 生成用户ftp资源文件
     * @param userId 用户id
     * @return /image/id/
     */
    private String generateFtpPath(final Integer userId) {
        return config.getFtpWorkPath() + userId + "/";
    }

    private String getSuffix(final String fileName) throws ServerException {
        if (fileName == null) {
            throw new ServerException(ServerExceptionBean.FILE_EXCEPTION);
        }
        if (fileName.endsWith(JPG_SUFFIX)) {
            return JPG_SUFFIX;
        }
        if (fileName.endsWith(PNG_SUFFIX)) {
            return PNG_SUFFIX;
        }
        throw new ServerException(ServerExceptionBean.FILE_EXCEPTION);
    }
}
