package org.study.error;

/**
 * @author fanqie
 * @date 2020/1/4
 */
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
    ORDER_FAIL_BY_AMOUNT_EXCEPTION(50000, "商品数量不正确"),
    /* 后端异常导致下单失败 */
    ORDER_FAIL_BY_SYSTEM_EXCEPTION(50001, "系统内部异常"),
    /* select结果为空 */
    ORDER_NOT_EXIST_EXCEPTION(50002, "订单不存在"),
    /* 修改订单状态出现异常或update语句影响的行数为0 */
    ORDER_STATUS_EXCEPTION(50003, "修改订单状态失败"),
    /* 取消订单时SQL更新语句影响行数小于1 */
    ORDER_CANCEL_EXCEPTION(50004, "暂时无法取消订单"),
    /* 插入失败 */
    ADDRESS_INFO_INSERT_EXCEPTION(60001, "无法插入新的地址信息"),
    /* 更新失败 */
    ADDRESS_INFO_UPDATE_EXCEPTION(60002, "无法更新地址信息"),
    /* 删除失败 */
    ADDRESS_INFO_DELETE_EXCEPTION(60003, "删除信息失败"),
    /* 重置的SQL语句执行失败 */
    ADDRESS_SELECTED_RESET_EXCEPTION(60004, "重置默认地址信息失败"),
    /* 默认地址信息未设置 */
    WITHOUT_DEFAULT_ADDRESS(60005, "未设置默认地址，无法下单"),
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
