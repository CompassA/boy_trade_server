package org.study.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * @author fanqie
 * @date 2020/1/9
 */
public final class MyStringUtil {

    private MyStringUtil() {
    }

    public static String encodeByBase64(final String data)
            throws IOException {
        return new BASE64Encoder().encode(
                new BASE64Decoder().decodeBuffer(data));
    }
}
