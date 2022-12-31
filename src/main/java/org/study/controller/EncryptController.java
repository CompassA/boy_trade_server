package org.study.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.study.controller.response.ServerResponse;
import org.study.service.EncryptService;

import javax.annotation.Resource;

/**
 * @author fanqie
 * Created on 2020/1/9
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class EncryptController {

    @Resource
    private EncryptService encryptService;

    @GetMapping(value = ApiPath.Encrypt.PUB_KEY)
    public ServerResponse getRsaPublicKey() {
        return ServerResponse.create(encryptService.getPublicKey());
    }
}
