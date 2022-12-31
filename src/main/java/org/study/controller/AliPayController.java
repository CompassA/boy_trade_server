package org.study.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.study.config.AliPayConfig;
import org.study.controller.response.AliPayFactory;
import org.study.controller.response.ServerRequest;
import org.study.error.ServerException;
import org.study.error.ServerExceptionEnum;
import org.study.service.EncryptService;
import org.study.service.OrderService;
import org.study.service.SessionService;
import org.study.util.MyTimeUtil;
import org.study.view.OrderVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author fanqie
 * Created on 2020/3/9
 */
@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class AliPayController {

    @Resource
    private AliPayConfig config;

    @Resource
    private SessionService sessionService;

    @Resource
    private EncryptService encryptService;

    @Resource
    private OrderService orderService;

    @PostMapping(ApiPath.Trade.PAY)
    public void trade(@RequestParam("userId") Integer userId, @RequestParam("token") String token,
            @RequestBody ServerRequest encryptedOrderData,
            HttpServletResponse response) throws ServerException {
        if (!sessionService.isLogin(token, userId)) {
            throw new ServerException(ServerExceptionEnum.USER_TRADE_INVALID_EXCEPTION);
        }

        final OrderVO orderVO = encryptService.deserialize(encryptedOrderData, OrderVO.class);
        if (orderService.isOrderExpired(MyTimeUtil.parseStr(orderVO.getCreateTime()))) {
            throw new ServerException(ServerExceptionEnum.ORDER_EXPIRED_EXCEPTION);
        }
        try {
            final AlipayClient client = AliPayFactory.getClient(config);
            final AlipayTradePagePayRequest request = AliPayFactory.getRequest(orderVO, config);
            final AlipayTradePagePayResponse aliPayResponse = client.pageExecute(request);
            final String body = aliPayResponse.getBody();
            response.setContentType(String.format("text/html;charset=%s", config.getCharset()));
            response.getWriter().write(body);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (final AlipayApiException | IOException e) {
            e.printStackTrace();
        }
    }

}
