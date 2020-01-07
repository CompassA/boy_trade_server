package org.study.response;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加密
 * @author fanqie
 * @date 2020/1/6
 */
@Component
public class EncryptBean {

    private static final String ALGORITHM = "RSA";

    /** 512位key size最多加密(512 / 8 - 11)字节数据*/
    private static final int KEY_SIZE = 512;

    @Getter
    private byte[] publicKey;

    private byte[] privateKey;

    public EncryptBean() {
        initKeyPair();
    }

    private void initKeyPair() {
        final KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance(ALGORITHM);
            generator.initialize(KEY_SIZE);
            final KeyPair keyPair = generator.generateKeyPair();
            publicKey = keyPair.getPublic().getEncoded();
            privateKey = keyPair.getPrivate().getEncoded();
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public byte[] encryptByPrivateKey(final byte[] data) {
        final PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        try {
            final PrivateKey privateKey = KeyFactory.getInstance(ALGORITHM)
                    .generatePrivate(pkcs8KeySpec);
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decryptByPrivateKey(final byte[] data) {
        final PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        try {
            final PrivateKey privateKey = KeyFactory.getInstance(ALGORITHM)
                    .generatePrivate(pkcs8KeySpec);
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decryptByPublicKey(final byte[] data) {
        try {
            //密钥材料转换
            final X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);

            //产生公钥
            final PublicKey pubKey = KeyFactory.getInstance(ALGORITHM)
                    .generatePublic(x509KeySpec);
            //数据解密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] encryptByPublicKey(final byte[] data) {
        try {
            //密钥材料转换
            final X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);

            //产生公钥
            final PublicKey pubKey = KeyFactory.getInstance(ALGORITHM)
                    .generatePublic(x509KeySpec);
            //数据解密
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
