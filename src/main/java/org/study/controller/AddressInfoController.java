package org.study.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.study.aspects.annotation.EnableTokenValidation;
import org.study.controller.response.ServerResponse;
import org.study.data.AddressInfoDO;
import org.study.error.ServerException;
import org.study.error.ServerExceptionEnum;
import org.study.service.AddressInfoService;

import javax.annotation.Resource;

/**
 * @author fanqie
 * Created on 2020/2/7
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class AddressInfoController {

    @Resource
    private AddressInfoService addressInfoService;

    @EnableTokenValidation
    @GetMapping(ApiPath.AddressInfo.GET_USER_ADDRESS_INFO)
    public ServerResponse getInfo(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token) throws ServerException {
        return ServerResponse.create(addressInfoService.getUserInfoModel(userId));
    }

    @EnableTokenValidation
    @PutMapping(ApiPath.AddressInfo.INSERT_USER_ADDRESS_INFO)
    public ServerResponse insertInfo(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestBody final AddressInfoDO info) throws ServerException {
        info.setUserId(userId);
        return ServerResponse.create(addressInfoService.addNewInfo(userId, info));
    }

    @EnableTokenValidation
    @DeleteMapping(ApiPath.AddressInfo.DELETE_USER_ADDRESS_INFO)
    public ServerResponse deleteInfo(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestParam("infoId") final Integer infoId) throws ServerException {
        return ServerResponse.create(addressInfoService.deleteInfo(userId, infoId));
    }

    @EnableTokenValidation
    @PostMapping(ApiPath.AddressInfo.UPDATE_USER_ADDRESS_INFO)
    public ServerResponse updateInfo(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestBody final AddressInfoDO info) throws ServerException {
        info.setUserId(userId);
        return ServerResponse.create(addressInfoService.updateInfo(userId, info));
    }

    @EnableTokenValidation
    @PostMapping(ApiPath.AddressInfo.RESET_SELECTED_ADDRESS)
    public ServerResponse updateSelected(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestParam("infoId") final Integer infoId) throws ServerException {
        return ServerResponse.create(addressInfoService.resetDefaultInfo(userId, infoId));
    }

    @EnableTokenValidation
    @GetMapping(ApiPath.AddressInfo.DEFAULT_INFO)
    public ServerResponse getDefaultInfo(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token) throws ServerException {
        return addressInfoService.getDefaultInfo(userId)
                .map(ServerResponse::create)
                .orElse(ServerResponse.fail(ServerExceptionEnum.WITHOUT_DEFAULT_ADDRESS));
    }
}
