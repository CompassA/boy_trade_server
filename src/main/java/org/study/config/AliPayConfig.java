package org.study.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author fanqie
 * @date 2020/3/9
 */
@Getter
@Setter
@Component
public class AliPayConfig {

    private final String appId = "2016101700709331";

    private final String merchantPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCv+dmUX/+1T//I7vHZF3T7h+HcX4R/+4BmR/KcNPLxCzQY2AiKbfIvsriTTa2YL+aLq6sEqJSfL58U2QncvtyNB2Ar5QH0/CdY/jTMxpU0VC3pSdUKUAfUlTfeoVrc0p/NESp35xee8pce4Kl3e8G17VpTO3dDP9NOdiz1uy90SaX6krLDie+NrDOBQtUe3q+s1s3V2LEWnk4nZSyKk9YEey2RMjLXhOhAWlNHPFjWe8W8Y4oEsuTmr899FmCNhUUmYAf1zGVdIxCdq6pYcEF0rDenuivPd4uTpuHsX95SI7WYMEpfzyhkYyL/sKqVTdqzV1kFpcCkJZ++ZB/+SVgBAgMBAAECggEAG3Pu7abMTYzdaotcT3HJXjQkmCyOjyi9LzeqMZRREimdRwLs0iu0zJwlD1Xe7w8J+GAfnepuxSa4upfhYf6kiZvM9QwU8hMkUE3YDtosDK3VO7+47JLvpcphnOmvMyPoqiUzauEyXimaScbiqTE1VxxgKvvr7yHV5Teqa+t3y7wumavseAC3zqc0rlCv+mSk3FkbWhskYzDJCJUIRjV9ySubKZ3CoR4TRViYBfBAG5jDegg+aHJpNWRTdB+zxWTlZqPK4DpNnLqQhhqTGhRDxmiaOV2C+sxFtdU0iq26br7mo0FpoyLIkV6+KnOd+QgNKVd5dl7P91UEqg4dw4BKwQKBgQD3NrzfqTIiixnw3Y3KCfi0avRbtqSPktCzvBMbvsiViKh7zf613YyzZ+rSIyVJFXi4ivI91BvRLSjboDXkVTojgOJmbTxbaxvr4uKlw9HusMQ6FlAx3HShN/4emrhH7x23qSH+F+teo9hK+Rzr+xpJrNxrfV+Vbz9IIQApEMm9AwKBgQC2OvVSAMX6S+kc16d2+SOyUY9SyujZ2DSom3GxkPU3tBN8v8XPiJMjiGn2KRdPjrYnkH+CQcvrEOzkBt0VIaQzYsGzD3GlNlSIeADgAc4NMJEJ8CsrazfJDaobDxgJDdtkChjKw8XZRcSLrTFhVyVjuD32ZmoRAxjr3p92SUpdqwKBgQCDmCm2TVSYur8WTnmZoPE46O4mm/TBUH+0DVuGm2oe314AeUkq+kJZhemj7n18h+d5JM2KSAx+rrFfJ8tIPm3yVDswFtnYOA3m5SGM584qZwItgY8y241h/HJTTDzoXcrN50RnA8iQ2lranfzdSQ9XRKMYvOZSn6Np+9cgBQW+jQKBgFal7iQRzhFXqv5V5IEDsd+GPHoHlXSQA2nwj7z4ykC0VVfJ8ArgS+ZnZMrxeu4PU7m4UYjkNEwPfjRmtxTnLCZq3OOBgm4mSUeil/fro8hNwYlSM5SvpJTq9GltYeZcOmd1DZjMQfUv4Wbw8TvxrguefYf95o00RdNRxcz+yGeFAoGBAIo0WsALAsI+a5ThXXCj9sMY3ROADBzKQREyxJ7d4dUc5vMdrQ8BVOk3qAt59EKCtTdVRLYCG2K1qcfQq4/vY0Tu+zTGiXsd0lydnlPxG+vItpg9iKu6gKo0meYaddG1A+R96qtyy/TZ37oMCGDRJ5VWJwClLPa+qv9pfpkExYz9";

    private final String aliPayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArpnt4T5ERuveUEOWHxKt28040OhxxARKPK2wJ4qodw2LJNQZ8vZsvozyoQPUFMmy4d7Rt/H7YYs+FDN+z2x4BPQ2zxrBaEW/Wi+xn7qLK+pSNt6dED+HYaZ8zZJH8cFGCi9p3fD6qkD8ZkIDmWoJiM4PPcSNERwJK9R9f2SlxSh7/sVLvJp98lp32xfHNoSszA6h4Olp5eCXcVxiuLtA5cGC6JhYvqrZGoYXY0vVGz6njodX0EW4eEufIfWgO2VS5rkP9XcyiYIoxVwnQ/opyvgrFn4kOjGa1FSCBedStUs/8HT7fgovYOfuxsLFFZUbAqwXN/JixqktUwnwGV1C1wIDAQAB";

    private final String gateWayUrl = "https://openapi.alipaydev.com/gateway.do";

    private final String notifyUrl = "";

    @Value("${alipay.return-url}")
    private String returnUrl;

    private final String signType = "RSA2";

    private final String charset = "UTF-8";

    private final String format = "JSON";

    private final String tradePropertyName = "out_trade_no";
}
