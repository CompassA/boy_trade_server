package org.study.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author fanqie
 * @date 2020/1/4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum ServerExceptionBean implements SystemException {
    /* 参数异常 */
    PARAMETER_VALIDATION_EXCEPTION(10000, "参数不合法"),
    /* 加密信息异常 */
    ENCRYPT_DECRYPT_EXCEPTION(10001, "密钥异常"),
    /* 订单序号生成失败 */
    SEQUENCE_EXCEPTION(10002, "序列生成失败"),
    /* 目标序列不存在 */
    SEQUENCE_NOT_EXIST_EXCEPTION(10003, "序列号不存在"),
    /* 用户不存在 */
    USER_LOGIN_EXCEPTION(20000, "用户不存在或密码错误"),
    /* 注册失败 */
    USER_REGISTRY_EXCEPTION(20001, "注册失败"),
    /* 查询用户信息失败 */
    USER_QUERY_EXCEPTION(20002, "查询用户信息失败"),
    /* 登录凭证缺失*/
    USER_NOT_LOGIN_EXCEPTION(20002, "用户未登录"),
    /* 非法支付 */
    USER_TRADE_INVALID_EXCEPTION(20003, "用户状态非法"),
    /* 商品创建失败 */
    PRODUCT_CREATE_EXCEPTION(30000, "商品创建失败"),
    /* 商品查询失败 */
    PRODUCT_NOT_EXIST_EXCEPTION(30001, "商品不存在"),
    /* 库存不足 */
    PRODUCT_STOCK_NOT_ENOUGH_EXCEPTION(30002, "商品库存不足"),
    /* 订单商品详情中的持有者与商品的持有者不同 */
    PRODUCT_OWNER_EXCEPTION(30003, "商品信息非法"),
    /* 文件上传异常 */
    FILE_EXCEPTION(40000, "文件上传失败"),
    /* 因库存不足导致下单失败 */
    ORDER_FAIL_BY_AMOUNT_EXCEPTION(50000, "下单失败，商品数量不正确"),
    /* 后端异常导致下单失败 */
    ORDER_FAIL_BY_SYSTEM_EXCEPTION(50001, "系统内部异常"),
    /* select结果为空 */
    ORDER_NOT_EXIST_EXCEPTION(50002, "订单不存在"),
    ;

    private final Integer errorCode;

    private final String message;

    ServerExceptionBean(final Integer errorCode, final String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
