package org.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.study.controller.response.ServerResponse;
import org.study.data.AddressInfoDO;
import org.study.error.ServerException;
import org.study.service.AddressInfoService;

/**
 * @author fanqie
 * @date 2020/2/7
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class AddressInfoController {

    @Autowired
    private AddressInfoService addressInfoService;

    @GetMapping(ApiPath.AddressInfo.GET_USER_ADDRESS_INFO)
    public ServerResponse getInfo(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token) {
        return ServerResponse.create(addressInfoService.getUserInfoModel(userId));
    }

    @PutMapping(ApiPath.AddressInfo.INSERT_USER_ADDRESS_INFO)
    public ServerResponse insertInfo(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestBody final AddressInfoDO info) throws ServerException {
        info.setUserId(userId);
        return ServerResponse.create(addressInfoService.addNewInfo(userId, info));
    }

    @DeleteMapping(ApiPath.AddressInfo.DELETE_USER_ADDRESS_INFO)
    public ServerResponse deleteInfo(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestParam("infoId") final Integer infoId) throws ServerException {
        return ServerResponse.create(addressInfoService.deleteInfo(userId, infoId));
    }

    @PostMapping(ApiPath.AddressInfo.UPDATE_USER_ADDRESS_INFO)
    public ServerResponse updateInfo(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestBody final AddressInfoDO info) throws ServerException {
        info.setUserId(userId);
        return ServerResponse.create(addressInfoService.updateInfo(userId, info));
    }

    @PostMapping(ApiPath.AddressInfo.RESET_SELECTED_ADDRESS)
    public ServerResponse updateSelected(
            @RequestParam("userId") final Integer userId,
            @RequestParam("infoId") final Integer infoId,
            @RequestParam("token") final String token) throws ServerException {
        return ServerResponse.create(addressInfoService.resetDefaultInfo(userId, infoId));
    }
}
