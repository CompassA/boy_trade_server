package org.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.study.controller.response.ServerResponse;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.CartService;
import org.study.service.SessionService;
import org.study.util.ModelToViewUtil;
import org.study.view.CartDTO;

/**
 * @author fanqie
 * @date 2020/2/9
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private SessionService sessionService;

    @GetMapping(ApiPath.Cart.GET)
    public ServerResponse getCartView(
            @RequestParam("token") final String token,
            @RequestParam("userId") final Integer userId) throws ServerException {
        if (!sessionService.isLogin(token, userId)) {
            throw new ServerException(ServerExceptionBean.CANNOT_PUT_WHEN_LOGOUT_EXCEPTION);
        }
        return ServerResponse.create(
                ModelToViewUtil.getCartVOList(cartService.getCartModel(userId)));
    }

    @PostMapping(ApiPath.Cart.ADD)
    public ServerResponse addProductToCart(
            @RequestParam("token") final String token,
            @RequestParam("userId") final Integer userId,
            @RequestBody final CartDTO cartDTO) throws ServerException {
        if (!sessionService.isLogin(token, userId) || !userId.equals(cartDTO.getUserId())) {
            throw new ServerException(ServerExceptionBean.CANNOT_PUT_WHEN_LOGOUT_EXCEPTION);
        }
        return cartService.addProduct(cartDTO)
                ? ServerResponse.create(null)
                : ServerResponse.fail(ServerExceptionBean.CART_ADD_EXCEPTION);
    }

    @DeleteMapping(ApiPath.Cart.DELETE)
    public ServerResponse deleteProductFromCart(
            @RequestParam("token") final String token,
            @RequestParam("userId") final Integer userId,
            @RequestBody final CartDTO cartDTO) throws ServerException {
        if (!sessionService.isLogin(token, userId) || !userId.equals(cartDTO.getUserId())) {
            throw new ServerException(ServerExceptionBean.CANNOT_PUT_WHEN_LOGOUT_EXCEPTION);
        }
        return cartService.deleteProduct(cartDTO)
                ? ServerResponse.create(null)
                : ServerResponse.fail(ServerExceptionBean.CART_DELETE_EXCEPTION);
    }
}
