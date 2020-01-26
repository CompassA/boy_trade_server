package org.study.error;

/**
 * @author fanqie
 * @date 2020/1/4
 */
public enum ServerExceptionBean implements SystemException {
    /** 参数异常 */
    PARAMETER_VALIDATION_EXCEPTION(10000, "参数不合法"),
    /** 加密信息异常 */
    ENCRYPT_DECRYPT_EXCEPTION(10001, "密钥异常"),
    /** 订单序号生成失败 */
    SEQUENCE_EXCEPTION(10002, "序列生成失败"),
    /** 目标序列不存在 */
    SEQUENCE_NOT_EXIST_EXCEPTION(10003, "序列号不存在"),
    /** 用户不存在 */
    USER_LOGIN_EXCEPTION(20000, "用户不存在或密码错误"),
    /** 注册失败 */
    USER_REGISTRY_EXCEPTION(20001, "注册失败"),
    /** 查询用户信息失败 */
    USER_QUERY_EXCEPTION(20002, "查询用户信息失败"),
    /** 登录凭证缺失*/
    USER_NOT_LOGIN_EXCEPTION(20002, "用户未登录"),
    /** 商品创建失败 */
    PRODUCT_CREATE_EXCEPTION(30000, "商品创建失败"),
    /** 商品查询失败 */
    PRODUCT_NOT_EXIST_EXCEPTION(30001, "商品不存在"),
    /** 库存不足 */
    PRODUCT_STOCK_NOT_ENOUGH_EXCEPTION(30002, "商品库存不足"),
    /** 文件上传异常 */
    FILE_EXCEPTION(40000, "文件上传失败"),
    /** 因库存不足导致下单失败 */
    ORDER_FAIL_BY_STOCK_EXCEPTION(50000, "下单失败，商品库存已经不足"),
    /** 后端异常导致下单失败 */
    ORDER_FAIL_BY_SYSTEM_EXCEPTION(50001, "系统内部异常"),
    ;

    private final Integer errCode;

    private final String errMsg;

    ServerExceptionBean(final Integer errCode, final String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }


    @Override
    public Integer getErrCode() {
        return errCode;
    }

    @Override
    public String getErrMsg() {
        return errMsg;
    }
}
