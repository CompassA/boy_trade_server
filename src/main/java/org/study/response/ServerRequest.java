package org.study.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.study.service.EncryptService;
import org.study.util.MyStringUtil;
import sun.misc.BASE64Decoder;

/**
 * @author fanqie
 * @date 2020/1/13
 */
@Getter
public class ServerRequest {

    private String key;

    private String encryptData;

    public ServerRequest(final String key, final String encryptData) {
        this.key = key;
        this.encryptData = encryptData;
    }

    public <T> T deserialize(
            final EncryptService encryptService,
            final Class<T> type) throws Exception {
        final byte[] aesKey = encryptService.decryptByPrivateKey(
                new BASE64Decoder().decodeBuffer(key));
        final String dataStr = encryptService.decryptByAesKey(encryptData, aesKey);
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(MyStringUtil.base64ToUtf8(dataStr), type);
    }
}
