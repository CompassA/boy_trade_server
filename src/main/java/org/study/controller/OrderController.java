package org.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.study.error.ServerException;
import org.study.error.ServerExceptionBean;
import org.study.model.OrderModel;
import org.study.response.ServerRequest;
import org.study.response.ServerResponse;
import org.study.service.EncryptService;
import org.study.service.OrderService;
import org.study.util.ViewToModelUtil;
import org.study.view.OrderDTO;

import java.util.Optional;

/**
 * @author fanqie
 * @date 2020/1/26
 */
@RestController
@CrossOrigin(
        allowCredentials = "true",
        allowedHeaders = "*",
        origins = {"http://localhost:8080"})
public class OrderController extends BaseController {

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private OrderService orderService;

    @PostMapping(value = ApiPath.Order.CREATE)
    public ServerResponse createOrder(
            @RequestBody final ServerRequest encryptData) throws ServerException {
        try {
            final OrderDTO orderDTO = encryptData.deserialize(encryptService, OrderDTO.class);
            final Optional<OrderModel> orderModel = ViewToModelUtil.getOrderModel(orderDTO);
            if (!orderModel.isPresent()) {
                throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_SYSTEM_EXCEPTION);
            }
//            final OrderModel result = orderService.createOrder(orderModel.get());
//            final Optional<OrderVO> view = ModelToViewUtil.getOrderVO(result);
//            if (view.isPresent()) {
//                return ServerResponse.create(view.get());
//            }
//            throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_SYSTEM_EXCEPTION);
            return ServerResponse.create(null);
        } catch (final ServerException e) {
            throw e;
        } catch (final Exception e) {
            e.printStackTrace();
            throw new ServerException(ServerExceptionBean.ORDER_FAIL_BY_SYSTEM_EXCEPTION);
        }
    }
}
