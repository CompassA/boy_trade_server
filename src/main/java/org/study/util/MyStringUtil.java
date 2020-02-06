package org.study.util;

import org.study.service.model.CacheType;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
        return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * BASE64 -> UTF-8
     * @param data BASE64编码的字符串
     * @return UTF-8编码的字符串
     */
    public static String base64ToUtf8(final String data) {
        return new String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8);
    }

    /**
     * 为缓存生成key
     * @param id 数据主键
     * @param type 数据类型
     * @return 生成的缓存key
     */
    public static String generateCacheKey(final Integer id, final CacheType type) {
        return String.format("%s:%d", type.getPrefix(), id);
    }
}
