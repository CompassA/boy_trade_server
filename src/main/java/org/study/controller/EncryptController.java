package org.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.study.response.ServerResponse;
import org.study.service.EncryptService;

/**
 * @author fanqie
 * @date 2020/1/9
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class EncryptController {

    @Autowired
    private EncryptService encryptService;

    @GetMapping(value = ApiPath.Encrypt.PUB_KEY)
    public ServerResponse getRsaPublicKey() {
        return ServerResponse.create(encryptService.getPublicKey());
    }
}
