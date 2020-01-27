package org.study.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.study.service.EncryptService;
import org.study.util.MyStringUtil;

import java.util.Base64;

/**
 * @author fanqie
 * @date 2020/1/13
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerRequest {

    private String key;

    private String encryptData;

    public ServerRequest(final String key, final String encryptData) {
        this.key = key;
        this.encryptData = encryptData;
    }

    /**
     * 解密并反序列化
     * @param encryptService 加解密服务
     * @param type 反序列化目标类型
     * @param <T> 反序列化返回目标类型
     * @return 反序列化对象
     * @throws Exception 反序列化或加解密异常
     */
    public <T> T deserialize(
            final EncryptService encryptService,
            final Class<T> type) throws Exception {
        //私钥解密得到aes key
        final byte[] aesKey = encryptService.decryptByPrivateKey(
                Base64.getDecoder().decode(key));
        //利用aes key解密json数据
        final String dataStr = encryptService.decryptByAesKey(encryptData, aesKey);
        //反序列化
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(MyStringUtil.base64ToUtf8(dataStr), type);
    }
}
