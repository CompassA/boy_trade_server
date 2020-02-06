package org.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.study.controller.response.ServerRequest;
import org.study.controller.response.ServerResponse;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.service.EncryptService;
import org.study.service.OrderService;
import org.study.service.SessionService;
import org.study.service.model.OrderModel;
import org.study.service.model.OrderStatus;
import org.study.service.model.UserModel;
import org.study.util.ModelToViewUtil;
import org.study.util.ViewToModelUtil;
import org.study.view.OrderDTO;
import org.study.view.OrderVO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fanqie
 * @date 2020/1/26
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class OrderController {

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SessionService sessionService;

    @PostMapping(value = ApiPath.Order.CREATE)
    public ServerResponse createOrder(
            @RequestParam("token") final String token,
            @RequestBody final ServerRequest encryptData) throws Exception {
        //未登录不可下单
        if (!sessionService.isLogin(token)) {
            throw new ServerException(ServerExceptionBean.USER_NOT_LOGIN_EXCEPTION);
        }

        //反序列化并校验
        final OrderDTO orderDTO = encryptData.deserialize(encryptService, OrderDTO.class);
        final Optional<OrderModel> orderModel = ViewToModelUtil.getOrderModel(orderDTO);
        if (!orderModel.isPresent()) {
            throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_SYSTEM_EXCEPTION);
        }

        //创建订单并返回前端订单状态
        final OrderModel result = orderService.createOrder(orderModel.get());
        return ModelToViewUtil.getOrderVO(result)
                .map(ServerResponse::create)
                .orElse(ServerResponse.fail(ServerExceptionBean.ORDER_FAIL_BY_SYSTEM_EXCEPTION));
    }

    @GetMapping(value = ApiPath.Order.QUERY_BY_ORDER_ID)
    public ServerResponse getOrderById(@RequestParam("orderId") final String orderId) {
        return orderService.selectOrderById(orderId)
                .flatMap(ModelToViewUtil::getOrderVO)
                .map(ServerResponse::create)
                .orElse(ServerResponse.fail(ServerExceptionBean.ORDER_NOT_EXIST_EXCEPTION));
    }

    @GetMapping(value = ApiPath.Order.CREATED_ORDER_WITH_SELLER)
    public ServerResponse getCreatedOrderWithSeller(
            @RequestParam("sellerId") final Integer sellerId,
            @RequestParam("token") final String token) throws ServerException {
        this.validateToken(sellerId, token);
        return ServerResponse.create(
                this.convertCore(orderService.selectCreatedOrderWithSeller(sellerId)));
    }

    @GetMapping(value = ApiPath.Order.PAID_ORDER_WITH_SELLER)
    public ServerResponse getPaidOrderWithSeller(
            @RequestParam("sellerId") final Integer sellerId,
            @RequestParam("token") final String token) throws ServerException {
        this.validateToken(sellerId, token);
        return ServerResponse.create(
                this.convertCore(orderService.selectPaidOrderWithSeller(sellerId)));
    }

    @GetMapping(value = ApiPath.Order.SENT_ORDER_WITH_SELLER)
    public ServerResponse getSentOrderWithSeller(
            @RequestParam("sellerId") final Integer sellerId,
            @RequestParam("token") final String token) throws ServerException {
        this.validateToken(sellerId, token);
        return ServerResponse.create(
                this.convertCore(orderService.selectSentOrderWithSeller(sellerId)));
    }

    @GetMapping(value = ApiPath.Order.FINISHED_ORDER_WITH_SELLER)
    public ServerResponse getFinishedOrderWithSeller(
            @RequestParam("sellerId") final Integer sellerId,
            @RequestParam("token") final String token) throws ServerException {
        this.validateToken(sellerId, token);
        return ServerResponse.create(
                this.convertCore(orderService.selectFinishedOrderWithSeller(sellerId)));
    }

    @GetMapping(value = ApiPath.Order.CREATED_ORDER_WITH_BUYER)
    public ServerResponse getCreatedOrderWithBuyer(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token) throws ServerException {
        this.validateToken(userId, token);
        return ServerResponse.create(this.convertCore(
                orderService.selectByUserId(userId, OrderStatus.CREATED, OrderStatus.CREATED)));
    }

    @GetMapping(value = ApiPath.Order.PAID_ORDER_WITH_BUYER)
    public ServerResponse getPaidOrderWithBuyer(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token) throws ServerException {
        this.validateToken(userId, token);
        return ServerResponse.create(this.convertCore(
                orderService.selectByUserId(userId, OrderStatus.PAID, OrderStatus.PAID)));
    }

    @GetMapping(value = ApiPath.Order.SENT_ORDER_WITH_BUYER)
    public ServerResponse getSentOrderWithBuyer(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token) throws ServerException {
        this.validateToken(userId, token);
        return ServerResponse.create(this.convertCore(
                orderService.selectByUserId(userId, OrderStatus.SENT, OrderStatus.PAID)));
    }

    @GetMapping(value = ApiPath.Order.FINISHED_ORDER_WITH_BUYER)
    public ServerResponse getFinishedOrderWithBuyer(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token) throws ServerException {
        this.validateToken(userId, token);
        return ServerResponse.create(this.convertCore(
                orderService.selectByUserId(userId, OrderStatus.FINISHED, OrderStatus.PAID)));
    }

    @GetMapping(value = ApiPath.Order.RECEIVED)
    public ServerResponse changeToFinishedStatus(
            @RequestParam("token") final String token,
            @RequestParam("userId") final Integer userId,
            @RequestParam("orderId") final String orderId) throws ServerException {
        this.validateToken(userId, token);
        return orderService.updateOrderStatus(orderId, OrderStatus.FINISHED, OrderStatus.PAID)
                ? ServerResponse.create(null)
                : ServerResponse.fail(ServerExceptionBean.ORDER_STATUS_EXCEPTION);
    }

    @GetMapping(value = ApiPath.Order.SENT_STATUS)
    public ServerResponse changeToSentStatus(
            @RequestParam("token") final String token,
            @RequestParam("userId") final Integer userId,
            @RequestParam("orderId") final String orderId) throws ServerException {
        this.validateToken(userId, token);
        return orderService.updateOrderStatus(orderId, OrderStatus.SENT, OrderStatus.PAID)
                ? ServerResponse.create(null)
                : ServerResponse.fail(ServerExceptionBean.ORDER_STATUS_EXCEPTION);
    }

    @GetMapping(value = ApiPath.Order.CANCEL)
    public ServerResponse changeToCancelStatus(
            @RequestParam("token") final String token,
            @RequestParam("userId") final Integer userId,
            @RequestParam("orderId") final String orderId) throws ServerException {
        this.validateToken(userId, token);
        orderService.cancelOrder(orderId, userId);
        return ServerResponse.create(null);
    }

    @PostMapping(value = ApiPath.Order.TRADE_PAY)
    public ServerResponse trade(
            @RequestParam("token") final String token,
            @RequestBody final ServerRequest request) throws Exception {
        final OrderVO orderVO = request.deserialize(encryptService, OrderVO.class);
        final Optional<UserModel> modelOpt = sessionService.getUserModel(token);
        //校验用户登录态
        if (!modelOpt.isPresent()) {
            throw new ServerException(ServerExceptionBean.USER_NOT_LOGIN_EXCEPTION);
        }
        //校验用户id
        final UserModel userModel = modelOpt.get();
        if (!userModel.getUserId().equals(orderVO.getUserId())) {
            throw new ServerException(ServerExceptionBean.USER_TRADE_INVALID_EXCEPTION);
        }

        //支付
        ;;;

        //更改订单状态
        return orderService.updateOrderStatus(
                orderVO.getOrderId(), OrderStatus.PAID, OrderStatus.PAID)
                ? ServerResponse.create(null)
                : ServerResponse.fail(ServerExceptionBean.ORDER_FAIL_BY_SYSTEM_EXCEPTION);
    }

    private List<OrderVO> convertCore(final List<OrderModel> models) {
        return models.stream()
                .map(ModelToViewUtil::getOrderVO)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private void validateToken(final Integer userId, final String token) throws ServerException {
        final Optional<UserModel> userInfoOpt = sessionService.getUserModel(token);
        if (!userInfoOpt.isPresent() || !userId.equals(userInfoOpt.get().getUserId())) {
            throw new ServerException(ServerExceptionBean.USER_TRADE_INVALID_EXCEPTION);
        }
    }
}
