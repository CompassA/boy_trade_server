package org.study.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 字符转码工具
 * @author fanqie
 * @date 2020/1/9
 */
public final class MyStringUtil {

    private MyStringUtil() {
    }

    /**
     * UTF-8 -> BASE64
     * @param data UTF-8编码的字符串
     * @return BASE64编码的字符串
     */
    public static String utf8ToBase64(final String data) {
        return new BASE64Encoder()
                .encode(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * BASE64 -> UTF-8
     * @param data BASE64编码的字符串
     * @return UTF-8编码的字符串
     * @throws IOException IO异常
     */
    public static String base64ToUtf8(final String data) throws IOException {
        return new String(
                new BASE64Decoder().decodeBuffer(data), StandardCharsets.UTF_8);
    }
}
