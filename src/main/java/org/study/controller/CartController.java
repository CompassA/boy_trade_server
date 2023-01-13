package org.study.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.study.aspects.annotation.EnableTokenValidation;
import org.study.controller.response.ServerResponse;
import org.study.error.ServerException;
import org.study.error.ServerExceptionEnum;
import org.study.service.CartService;
import org.study.util.ModelToViewUtil;
import org.study.view.CartDTO;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fanqie
 * Created on 2020/2/9
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class CartController {

    @Resource
    private CartService cartService;

    @EnableTokenValidation
    @GetMapping(ApiPath.Cart.GET)
    public ServerResponse getCartView(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token) throws ServerException {
        return ServerResponse.create(
                ModelToViewUtil.getCartVOList(cartService.getCartModel(userId)));
    }

    @EnableTokenValidation
    @PostMapping(ApiPath.Cart.ADD)
    public ServerResponse addProductToCart(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestBody final CartDTO cartDTO) throws ServerException {
        if (!userId.equals(cartDTO.getUserId())) {
            throw new ServerException(ServerExceptionEnum.CANNOT_PUT_WHEN_LOGOUT_EXCEPTION);
        }
        return cartService.addProduct(cartDTO)
                ? ServerResponse.create(null)
                : ServerResponse.fail(ServerExceptionEnum.CART_ADD_EXCEPTION);
    }

    @EnableTokenValidation
    @DeleteMapping(ApiPath.Cart.DELETE)
    public ServerResponse deleteProductFromCart(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestBody final CartDTO cartDTO) throws ServerException {
        if (!userId.equals(cartDTO.getUserId())) {
            throw new ServerException(ServerExceptionEnum.CANNOT_PUT_WHEN_LOGOUT_EXCEPTION);
        }
        return cartService.deleteProduct(cartDTO)
                ? ServerResponse.create(null)
                : ServerResponse.fail(ServerExceptionEnum.CART_DELETE_EXCEPTION);
    }

    @EnableTokenValidation
    @DeleteMapping(ApiPath.Cart.DELETE_CART)
    public ServerResponse deleteCart(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestBody final List<Integer> productIds) throws ServerException {
        return cartService.deleteCart(userId, productIds)
                ? ServerResponse.create(null)
                : ServerResponse.fail(ServerExceptionEnum.CART_DELETE_EXCEPTION);
    }
}
