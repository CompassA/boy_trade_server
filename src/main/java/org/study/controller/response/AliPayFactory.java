package org.study.controller.response;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.study.config.AliPayConfig;
import org.study.view.OrderVO;

/**
 * @author fanqie
 * @date 2020/3/9
 */
public final class AliPayFactory {

    private AliPayFactory() {
    }

    public static AlipayClient getClient(final AliPayConfig config) {
        return new DefaultAlipayClient(
                config.getGateWayUrl(),
                config.getAppId(),
                config.getMerchantPrivateKey(),
                config.getFormat(),
                config.getCharset(),
                config.getAliPayPublicKey(),
                config.getSignType());
    }

    public static AlipayTradePagePayRequest getRequest(OrderVO orderVO, AliPayConfig config) {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(config.getReturnUrl());
        request.setNotifyUrl(config.getNotifyUrl());
        request.setBizContent("{" +
                "    \"out_trade_no\":\"" + orderVO.getOrderId() + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + orderVO.getOrderAmount() + "," +
                "    \"subject\":\"" + orderVO.getUserName() + "\"," +
                "    \"body\":\"pay\"" +
                "  }");
        return request;
    }
}
