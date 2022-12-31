package org.study.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.study.aspects.annotation.EnableTokenValidation;
import org.study.cache.LocalCacheBean;
import org.study.config.AliPayConfig;
import org.study.controller.response.ServerRequest;
import org.study.controller.response.ServerResponse;
import org.study.error.ServerException;
import org.study.error.ServerExceptionEnum;
import org.study.limiter.BucketLimiter;
import org.study.mq.Producer;
import org.study.service.EncryptService;
import org.study.service.OrderService;
import org.study.service.ProductService;
import org.study.service.model.OrderModel;
import org.study.service.model.enumdata.OrderStatus;
import org.study.util.ModelToViewUtil;
import org.study.util.ViewToModelUtil;
import org.study.view.OrderDTO;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

/**
 * @author fanqie
 * Created on 2020/1/26
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class OrderController {

    @Resource
    private EncryptService encryptService;

    @Resource
    private OrderService orderService;

    @Resource
    private ProductService productService;

    @Resource
    private Producer producer;

    @Resource
    private AliPayConfig aliPayConfig;

    @Resource
    private LocalCacheBean cache;

    private BucketLimiter orderCreateLimiter;

    @PostConstruct
    public void init() {
        orderCreateLimiter = new BucketLimiter(30, 30, 1);
    }

    @EnableTokenValidation
    @PostMapping(value = ApiPath.Order.CREATE)
    public ServerResponse createOrder(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestBody final ServerRequest encryptData) throws Exception {
        //限流
        if (!orderCreateLimiter.acquire()) {
            throw new ServerException(ServerExceptionEnum.ORDER_CREATE_LIMIT_EXCEPTION);
        }

        //反序列化并校验
        final OrderDTO orderDTO = encryptService.deserialize(encryptData, OrderDTO.class);
        final Optional<OrderModel> orderModel = ViewToModelUtil.getOrderModel(orderDTO);
        if (!orderModel.isPresent()) {
            throw new ServerException(ServerExceptionEnum.ORDER_FAIL_BY_SYSTEM_EXCEPTION);
        }

        //创建订单并返回前端订单状态
        final OrderModel result = producer.createOrder(orderModel.get());
        return ModelToViewUtil.getOrderVO(result)
                .map(ServerResponse::create)
                .orElse(ServerResponse.fail(ServerExceptionEnum.ORDER_FAIL_BY_SYSTEM_EXCEPTION));
    }

    @GetMapping(value = ApiPath.Order.QUERY_BY_ORDER_ID)
    public ServerResponse getOrderById(@RequestParam("orderId") final String orderId) {
        return orderService.selectOrderById(orderId)
                .flatMap(ModelToViewUtil::getOrderVO)
                .map(ServerResponse::create)
                .orElse(ServerResponse.fail(ServerExceptionEnum.ORDER_NOT_EXIST_EXCEPTION));
    }

    @EnableTokenValidation
    @GetMapping(value = ApiPath.Order.CREATED_ORDER_WITH_SELLER)
    public ServerResponse getCreatedOrderWithSeller(
            @RequestParam("sellerId") final Integer sellerId,
            @RequestParam("token") final String token) throws ServerException {
        return ServerResponse.create(ModelToViewUtil.orderModelsToViews(
                orderService.selectCreatedOrderWithSeller(sellerId)));
    }

    @EnableTokenValidation
    @GetMapping(value = ApiPath.Order.PAID_ORDER_WITH_SELLER)
    public ServerResponse getPaidOrderWithSeller(
            @RequestParam("sellerId") final Integer sellerId,
            @RequestParam("token") final String token) throws ServerException {
        return ServerResponse.create(ModelToViewUtil.orderModelsToViews(
                orderService.selectPaidOrderWithSeller(sellerId)));
    }

    @EnableTokenValidation
    @GetMapping(value = ApiPath.Order.SENT_ORDER_WITH_SELLER)
    public ServerResponse getSentOrderWithSeller(
            @RequestParam("sellerId") final Integer sellerId,
            @RequestParam("token") final String token) throws ServerException {
        return ServerResponse.create(ModelToViewUtil.orderModelsToViews(
                orderService.selectSentOrderWithSeller(sellerId)));
    }

    @EnableTokenValidation
    @GetMapping(value = ApiPath.Order.FINISHED_ORDER_WITH_SELLER)
    public ServerResponse getFinishedOrderWithSeller(
            @RequestParam("sellerId") final Integer sellerId,
            @RequestParam("token") final String token) throws ServerException {
        return ServerResponse.create(ModelToViewUtil.orderModelsToViews(
                orderService.selectFinishedOrderWithSeller(sellerId)));
    }

    @EnableTokenValidation
    @GetMapping(value = ApiPath.Order.CREATED_ORDER_WITH_BUYER)
    public ServerResponse getCreatedOrderWithBuyer(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token) throws ServerException {
        return ServerResponse.create(ModelToViewUtil.orderModelsToViews(
                orderService.selectByUserId(userId, OrderStatus.CREATED, OrderStatus.CREATED)));
    }

    @EnableTokenValidation
    @GetMapping(value = ApiPath.Order.PAID_ORDER_WITH_BUYER)
    public ServerResponse getPaidOrderWithBuyer(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token) throws ServerException {
        return ServerResponse.create(ModelToViewUtil.orderModelsToViews(
                orderService.selectByUserId(userId, OrderStatus.PAID, OrderStatus.PAID)));
    }

    @EnableTokenValidation
    @GetMapping(value = ApiPath.Order.SENT_ORDER_WITH_BUYER)
    public ServerResponse getSentOrderWithBuyer(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token) throws ServerException {
        return ServerResponse.create(ModelToViewUtil.orderModelsToViews(
                orderService.selectByUserId(userId, OrderStatus.SENT, OrderStatus.PAID)));
    }

    @EnableTokenValidation
    @GetMapping(value = ApiPath.Order.FINISHED_ORDER_WITH_BUYER)
    public ServerResponse getFinishedOrderWithBuyer(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token) throws ServerException {
        return ServerResponse.create(ModelToViewUtil.orderModelsToViews(
                orderService.selectByUserId(userId, OrderStatus.FINISHED, OrderStatus.PAID)));
    }

    @EnableTokenValidation
    @GetMapping(value = ApiPath.Order.RECEIVED)
    public ServerResponse changeToFinishedStatus(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestParam("orderId") final String orderId) throws ServerException {
        return orderService.updateOrderStatus(orderId, OrderStatus.FINISHED, OrderStatus.PAID)
                ? ServerResponse.create(null)
                : ServerResponse.fail(ServerExceptionEnum.ORDER_STATUS_EXCEPTION);
    }

    @EnableTokenValidation
    @GetMapping(value = ApiPath.Order.SENT_STATUS)
    public ServerResponse changeToSentStatus(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestParam("orderId") final String orderId) throws ServerException {
        return orderService.updateOrderStatus(orderId, OrderStatus.SENT, OrderStatus.PAID)
                ? ServerResponse.create(null)
                : ServerResponse.fail(ServerExceptionEnum.ORDER_STATUS_EXCEPTION);
    }

    @EnableTokenValidation
    @GetMapping(value = ApiPath.Order.CANCEL)
    public ServerResponse changeToCancelStatus(
            @RequestParam("userId") final Integer userId,
            @RequestParam("token") final String token,
            @RequestParam("orderId") final String orderId) throws ServerException {
        orderService.cancelOrder(orderId);
        return ServerResponse.create(null);
    }

    @GetMapping(value = ApiPath.Order.TRADE_PAY)
    public String changeToPaidStatus(HttpServletRequest request) throws Exception {
        Map<String, String> params = Maps.newHashMap();
        request.getParameterMap().forEach((name, values) -> {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                if (i == values.length - 1) {
                    builder.append(values[i]);
                } else {
                    builder.append(values[i]).append(",");
                }
            }
            params.put(name, builder.toString());
        });

        boolean signVerified = AlipaySignature.rsaCheckV1(params, aliPayConfig.getAliPayPublicKey(),
                aliPayConfig.getCharset(), aliPayConfig.getSignType());

        if (signVerified) {
            final String orderId = new String(
                    request.getParameter(aliPayConfig.getTradePropertyName())
                            .getBytes(StandardCharsets.ISO_8859_1),
                    aliPayConfig.getCharset());

            //increase paid num, remove product if all buyer were paid
            orderService.selectOrderById(orderId).map(OrderModel::getProductDetails)
                    .ifPresent(detailModels -> detailModels.forEach(detailModel -> {
                        final Integer id = detailModel.getProductId();
                        productService.increasePaidNum(id, detailModel.getProductAmount());
                        if (productService.isProductAllPaid(id)) {
                            productService.removeProduct(id);
                            cache.invalidatePageCache();
                        }
                    }));

            orderService.updateOrderStatus(orderId, OrderStatus.PAID, OrderStatus.PAID);
        } else {
            return "失败";
        }
        return "成功";
    }
}
