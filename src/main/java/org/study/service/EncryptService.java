package org.study.service;

import org.study.error.ServerException;

/**
 * @author fanqie
 * @date 2020/1/8
 */
public interface EncryptService {

    /**
     * 公钥解密
     * @param data 待解密数据
     * @return 解密数据
     * @throws  Exception 解密异常，不描述具体信息
     */
    byte[] decryptByPublicKey(final byte[] data) throws Exception;

    /**
     * 公钥解密
     * @param data 待解密数据
     * @return 解密数据
     * @throws  Exception 解密异常，不描述具体信息
     */
    String decryptByPublicKey(final String data) throws Exception;

    /**
     * 公钥加密
     * @param data 待加密数据
     * @return 加密数据
     * @throws  Exception 解密异常，不描述具体信息
     */
    byte[] encryptByPublicKey(final byte[] data) throws Exception;

    /**
     * 公钥加密
     * @param data 待加密数据
     * @return 加密数据
     * @throws  Exception 解密异常，不描述具体信息
     */
    String encryptByPublicKey(final String data) throws Exception;

    /**
     * 私钥解密
     * @param data 待解密数据
     * @return 解密数据
     * @throws  Exception 解密异常，不描述具体信息
     */
    byte[] decryptByPrivateKey(final byte[] data) throws Exception;

    /**
     * 私钥解密
     * @param data 待解密数据
     * @return 解密数据
     * @throws  Exception 解密异常，不描述具体信息
     */
    String decryptByPrivateKey(final String data) throws Exception;

    /**
     * 私钥加密
     * @param data 待加密数据
     * @return 加密数据
     * @throws  Exception 解密异常，不描述具体信息
     */
    byte[] encryptByPrivateKey(final byte[] data) throws Exception;

    /**
     * 私钥加密
     * @param data 待加密数据
     * @return 加密数据
     * @throws  Exception 解密异常，不描述具体信息
     */
    String encryptByPrivateKey(final String data) throws Exception;

    /**
     * AES解密
     * @param data 待解密数据
     * @param aesKey AES密钥
     * @return 解密数据
     * @throws Exception 解密异常，不描述具体信息
     */
    byte[] decryptByAesKey(final byte[] data, final byte[] aesKey) throws Exception;

    /**
     * AES解密
     * @param data 待解密数据
     * @param aesKey AES密钥
     * @return 解密数据
     * @throws Exception 解密异常，不描述具体信息
     */
    String decryptByAesKey(final String data, final byte[] aesKey) throws Exception;

    /**
     * AES加密
     * @param data 待加密数据
     * @param aesKey AES密钥
     * @return 加密数据
     * @throws Exception 解密异常，不描述具体信息
     */
    byte[] encryptByAesKey(final byte[] data, final byte[] aesKey) throws Exception;

    /**
     * AES加密
     * @param data 待加密数据
     * @param aesKey AES密钥
     * @return 加密数据
     * @throws Exception 解密异常，不描述具体信息
     */
    String encryptByAesKey(final String data, final byte[] aesKey) throws Exception;

    /**
     * 返回公钥
     * @return 公钥的字符串形式
     */
    String getPublicKey();

    /**
     * MD5加密数据
     * @param data 待加密字符串
     * @return 加密后用BASE64编码的数据
     * @throws ServerException 加密失败
     */
    String encryptByMd5(final String data) throws ServerException;
}
