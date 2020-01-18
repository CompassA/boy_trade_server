package org.study.service.impl;

import org.springframework.stereotype.Component;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.EncryptService;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加密
 * 字符首先要经过base64转换
 * 加密链路： 待加密数据 -> base64转换为byte数组 -> byte加密 -> base64转换为加密字符串
 * 解密链路： base64将加密字符串转换为加密的byte数组 -> byte解密 -> base64转换为字符串
 * @author fanqie
 * @date 2020/1/6
 */
@Component
public class EncryptServiceImpl implements EncryptService {

    private static final String RSA_ALGORITHM = "RSA";

    private static final String AES_ALGORITHM = "AES";

    private static final String MD5_ALGORITHM = "MD5";

    private static final String AES_MODE = "AES/ECB/PKCS5Padding";

    /** 512位key size最多加密(512 / 8 - 11)字节数据*/
    private static final int KEY_SIZE = 512;

    private byte[] publicKey;

    private byte[] privateKey;

    public EncryptServiceImpl() {
        initKeyPair();
    }

    private void initKeyPair() {
        final KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            generator.initialize(KEY_SIZE);
            final KeyPair keyPair = generator.generateKeyPair();
            publicKey = keyPair.getPublic().getEncoded();
            privateKey = keyPair.getPrivate().getEncoded();
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String encryptByPrivateKey(final String data) throws Exception {
        final BASE64Decoder decoder = new BASE64Decoder();
        final BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(
                encryptByPrivateKey(decoder.decodeBuffer(data)));
    }

    @Override
    public String decryptByPrivateKey(final String data) throws Exception {
        final BASE64Decoder decoder = new BASE64Decoder();
        final BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(
                decryptByPrivateKey(decoder.decodeBuffer(data)));
    }

    @Override
    public String encryptByPublicKey(final String data) throws Exception {
        final BASE64Decoder decoder = new BASE64Decoder();
        final BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(
                encryptByPublicKey(decoder.decodeBuffer(data)));
    }

    @Override
    public String decryptByPublicKey(final String data) throws Exception {
        final BASE64Decoder decoder = new BASE64Decoder();
        final BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(
                decryptByPublicKey(decoder.decodeBuffer(data)));
    }

    @Override
    public String decryptByAesKey(final String data, final byte[] aesKey)
            throws Exception {
        final BASE64Decoder decoder = new BASE64Decoder();
        final BASE64Encoder encoder = new BASE64Encoder();
        return encoder
                .encode(decryptByAesKey(decoder.decodeBuffer(data), aesKey));
    }

    @Override
    public String encryptByAesKey(final String data, final byte[] aesKey)
            throws Exception {
        final BASE64Decoder decoder = new BASE64Decoder();
        final BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(
                encryptByAesKey(decoder.decodeBuffer(data), aesKey));
    }

    @Override
    public byte[] encryptByPrivateKey(final byte[] data) throws ServerException {
        final PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        try {
            final PrivateKey privateKey = KeyFactory.getInstance(RSA_ALGORITHM)
                    .generatePrivate(pkcs8KeySpec);
            final Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new ServerException(ServerExceptionBean.ENCRYPT_DECRYPT_EXCEPTION);
        }
    }

    @Override
    public byte[] decryptByPrivateKey(final byte[] data) throws ServerException {
        final PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        try {
            final PrivateKey privateKey = KeyFactory.getInstance(RSA_ALGORITHM)
                    .generatePrivate(pkcs8KeySpec);
            final Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new ServerException(ServerExceptionBean.ENCRYPT_DECRYPT_EXCEPTION);
        }
    }

    @Override
    public byte[] decryptByPublicKey(final byte[] data) throws ServerException {
        try {
            //密钥材料转换
            final X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);

            //产生公钥
            final PublicKey pubKey = KeyFactory.getInstance(RSA_ALGORITHM)
                    .generatePublic(x509KeySpec);

            //数据解密
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new ServerException(ServerExceptionBean.ENCRYPT_DECRYPT_EXCEPTION);
        }
    }

    @Override
    public byte[] encryptByPublicKey(final byte[] data) throws ServerException {
        try {
            //密钥材料转换
            final X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);

            //产生公钥
            final PublicKey pubKey = KeyFactory.getInstance(RSA_ALGORITHM)
                    .generatePublic(x509KeySpec);

            //数据解密
            final Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new ServerException(ServerExceptionBean.ENCRYPT_DECRYPT_EXCEPTION);
        }
    }

    @Override
    public byte[] encryptByAesKey(final byte[] data, final byte[] aesKey)
            throws ServerException {
        try {
            final SecretKey secretKey = new SecretKeySpec(aesKey, AES_ALGORITHM);
            final Cipher cipher = Cipher.getInstance(AES_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new ServerException(ServerExceptionBean.ENCRYPT_DECRYPT_EXCEPTION);
        }
    }

    @Override
    public byte[] decryptByAesKey(final byte[] data, final byte[] aesKey)
            throws ServerException {
        try {
            final SecretKey secretKey = new SecretKeySpec(aesKey, AES_ALGORITHM);
            final Cipher cipher = Cipher.getInstance(AES_MODE);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new ServerException(ServerExceptionBean.ENCRYPT_DECRYPT_EXCEPTION);
        }
    }

    @Override
    public String getPublicKey() {
        return new BASE64Encoder().encode(publicKey);
    }

    @Override
    public String encryptByMd5(final String data) throws ServerException {
        try {
            final MessageDigest md5 = MessageDigest.getInstance(MD5_ALGORITHM);
            final BASE64Encoder base64en = new BASE64Encoder();
            return base64en.encode(
                    md5.digest(
                            data.getBytes(StandardCharsets.UTF_8)));
        } catch (final Exception e) {
            e.printStackTrace();
            throw new ServerException(ServerExceptionBean.ENCRYPT_DECRYPT_EXCEPTION);
        }
    }

    public byte[] generateAesKey() throws NoSuchAlgorithmException {
        final KeyGenerator generator = KeyGenerator.getInstance(AES_ALGORITHM);
        generator.init(128);
        SecretKey sk = generator.generateKey();
        return sk.getEncoded();
    }
}
