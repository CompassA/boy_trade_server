package org.study.service.impl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传配置
 * @author fanqie
 * @date 2020/1/19
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "file-load-setting", ignoreUnknownFields = true)
public class FileServiceConfig {

    private String imageDictionary;
}
