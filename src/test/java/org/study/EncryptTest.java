package org.study;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.study.response.EncryptBean;

/**
 * @author fanqie
 * @date 2020/1/7
 */
public class EncryptTest extends BaseTest {

    @Autowired
    private EncryptBean encryptBean;

    @Test
    public void publicEncryptTest() {
        //53字节数据
        final String text = "12345678901234567890123456789012345678901234567890123";
        final byte[] encryptData = encryptBean.encryptByPublicKey(text.getBytes());

        final byte[] decryptData = encryptBean.decryptByPrivateKey(encryptData);

        Assert.assertEquals(text, new String(decryptData));
    }

    @Test
    public void privateEncryptTest() {
        final String text = "21212121232";
        final byte[] encryptData = encryptBean.encryptByPrivateKey(text.getBytes());

        final byte[] decryptData = encryptBean.decryptByPublicKey(encryptData);

        Assert.assertEquals(text, new String(decryptData));
    }
}
