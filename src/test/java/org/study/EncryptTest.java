package org.study;

import org.junit.Assert;
import org.junit.Test;
import org.study.controller.response.ServerRequest;
import org.study.error.ServerException;
import org.study.service.impl.EncryptServiceImpl;
import org.study.util.MyStringUtil;
import org.study.view.UserVO;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Objects;

/**
 * @author fanqie
 * Created on 2020/1/7
 */
public class EncryptTest extends BaseTest {

    @Resource
    private EncryptServiceImpl encryptServiceImpl;

    @Test
    public void publicEncryptTest() throws ServerException {
        //53字节数据
        final String text = "12345678901234567890123456789012345678901234567890123";
        final byte[] encryptData = encryptServiceImpl.encryptByPublicKey(text.getBytes());

        final byte[] decryptData = encryptServiceImpl.decryptByPrivateKey(encryptData);

        Assert.assertEquals(text, new String(decryptData));
    }

    @Test
    public void privateEncryptTest() throws ServerException {
        final String text = "21212121232";
        final byte[] encryptData = encryptServiceImpl.encryptByPrivateKey(text.getBytes());

        final byte[] decryptData = encryptServiceImpl.decryptByPublicKey(encryptData);

        Assert.assertEquals(text, new String(decryptData));
    }

    @Test
    public void aesTest() throws ServerException, UnsupportedEncodingException {
        final byte[] key = "abcdef1234567890".getBytes();
        final String text = "fwaefawefewafewfasdfsdacvsdacvsdavsadwfweacsdccccccCsac" +
                "sdaf" +
                "dsaf" +
                "sdf" +
                "dsaffffffffffffffasdfew" +
                "afaw" +
                "cweafggggggggggggggweafwa";

        final String charset = "utf-8";
        final byte[] encryptData = encryptServiceImpl
                .encryptByAesKey(text.getBytes(charset), key);
        final byte[] decryptData = encryptServiceImpl
                .decryptByAesKey(encryptData, key);

        Assert.assertEquals(new String(decryptData, charset), text);
    }

    @Test
    public void aesStringInterfaceTest() throws Exception {
        final byte[] key = "waefwaffewfd21df".getBytes();
        final String text = "fwaefawefewafewfasdfadawdwaqwedwqdafwafwafeawgewafwerfewafewafsadf" +
                "fscfafwdweewfwaefewfewf" +
                "weafweafewafweafvweaveeawgfewafew" +
                "ewafawefewafweafewafewafeawfdsavfdgewafewafeawfewfawefewfewafewfwea" +
                "eawfawefewafewafweafewafewafdsavadvawefaewfwae" +
                "ewfawefweafweafweafwfaewfawe" +
                "dewafewffffffffffffffffffffff";
        final String base64Text = MyStringUtil.utf8ToBase64(text);
        final String encryptStr = encryptServiceImpl.encryptByAesKey(base64Text, key);
        final String decryptStr = encryptServiceImpl.decryptByAesKey(encryptStr, key);
        Assert.assertEquals(text, MyStringUtil.base64ToUtf8(decryptStr));
    }

    @Test
    public void rsaStringInterfaceTest() throws Exception {
        final String text = "fwaefawefewafeawefeawfweafwea";
        final String base64Text = MyStringUtil.utf8ToBase64(text);
        final String encryptStr = encryptServiceImpl.encryptByPublicKey(base64Text);
        final String decryptStr = encryptServiceImpl.decryptByPrivateKey(encryptStr);
        Assert.assertEquals(text, MyStringUtil.base64ToUtf8(decryptStr));

        final String encryptStr2 = encryptServiceImpl.encryptByPrivateKey(base64Text);
        final String decryptStr2 = encryptServiceImpl.decryptByPublicKey(encryptStr2);
        Assert.assertEquals(text, MyStringUtil.base64ToUtf8(decryptStr2));
    }

    @Test
    public void serverRequestTest() throws Exception {
        final String userJson = "{\n" +
                "  \"userId\": 12,\n" +
                "  \"account\": \"admin\",\n" +
                "  \"name\": \"番茄\",\n" +
                "  \"iconUrl\": \"asdawdawd\"\n" +
                "}";
        final byte[] aesKey = encryptServiceImpl.generateAesKey();

        final String encryptJson = encryptServiceImpl.encryptByAesKey(
                MyStringUtil.utf8ToBase64(userJson), aesKey);
        final String key = Base64.getEncoder().encodeToString(
                encryptServiceImpl.encryptByPublicKey(aesKey));

        final ServerRequest mock = new ServerRequest(key, encryptJson);
        final UserVO userVO = encryptServiceImpl.deserialize(mock, UserVO.class);
        Assert.assertTrue(Objects.nonNull(userVO));
        System.out.println(userVO);
    }
}
