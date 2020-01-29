package org.study.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.model.OrderModel;
import org.study.response.ServerRequest;
import org.study.response.ServerResponse;
import org.study.service.EncryptService;
import org.study.service.OrderService;
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
public class OrderController extends BaseController {

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private OrderService orderService;

    @PostMapping(value = ApiPath.Order.CREATE)
    public ServerResponse createOrder(
            @RequestBody final ServerRequest encryptData) throws ServerException {
        try {
            //反序列化并校验
            final OrderDTO orderDTO = encryptData.deserialize(encryptService, OrderDTO.class);
            final Optional<OrderModel> orderModel = ViewToModelUtil.getOrderModel(orderDTO);
            if (!orderModel.isPresent()) {
                throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_SYSTEM_EXCEPTION);
            }

            //创建订单并返回前端订单状态
            final OrderModel result = orderService.createOrder(orderModel.get());
            final Optional<OrderVO> view = ModelToViewUtil.getOrderVO(result);
            if (view.isPresent()) {
                return ServerResponse.create(view.get());
            }
            throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_SYSTEM_EXCEPTION);
        } catch (final ServerException e) {
            throw e;
        } catch (final Exception e) {
            e.printStackTrace();
            throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_SYSTEM_EXCEPTION);
        }
    }

    @GetMapping(value = ApiPath.Order.QUERY_BY_USER_ID)
    public ServerResponse getOrderByUserId(
            @RequestParam("userId") final Integer userId) throws ServerException {
        final List<OrderModel> orderModels = orderService.selectOrdersByUserId(userId);
        final List<OrderVO> orders = orderModels.stream()
                .map(ModelToViewUtil::getOrderVO)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        if (orders.size() != orderModels.size()) {
            throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_SYSTEM_EXCEPTION);
        }
        return ServerResponse.create(orders);
    }

    @GetMapping(value = ApiPath.Order.QUERY_BY_ORDER_ID)
    public ServerResponse getOrderById(@Param("orderId") final String orderId) {
        return ModelToViewUtil.getOrderVO(orderService.selectOrderById(orderId).get())
                .map(ServerResponse::create)
                .orElse(ServerResponse.create(
                        ServerExceptionBean.ORDER_FAIL_BY_SYSTEM_EXCEPTION, "fail"));
    }
}
