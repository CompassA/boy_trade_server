package org.study.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.study.service.model.enumdata.CacheType;
import org.study.service.model.enumdata.PermanentKey;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 字符转码工具
 * @author fanqie
 * Created on 2020/1/9
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
    public static String getCacheKey(final Integer id, final CacheType type) {
        return String.format("%s:%d", type.getPrefix(), id);
    }

    /**
     * 为在redis持久存储的数据生成key
     * @param id 数据主键
     * @param type 持久存储的数据类型
     * @return 生成的持久数据key
     */
    public static String getPermanentKey(final Integer id, final PermanentKey type) {
        return String.format("p:%s:%d", type.getPrefix(), id);
    }

    /**
     * 根据用户编号生成分库分表位
     * @param userId 用户编号
     * @return 高16位低16位相异或并模100
     */
    public static String userIdMod(final Integer userId) {
        return String.format("%02d", ((userId) ^ (userId >>> 16)) % 100);
    }

    public static <K, V> byte[] mapToByte(final Map<K, V> map) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        final String mapJson = mapper.writeValueAsString(map);
        return mapJson.getBytes(StandardCharsets.UTF_8);
    }

    public static <K, V> Map<K, V> byteToMap(final byte[] bytes) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final String json = new String(bytes, StandardCharsets.UTF_8);
        return mapper.readValue(json, HashMap.class);
    }
}
