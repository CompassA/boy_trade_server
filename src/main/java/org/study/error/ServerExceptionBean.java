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
    /** 用户不存在 */
    USER_LOGIN_EXCEPTION(20000, "用户不存在或密码错误"),
    /** 注册失败 */
    USER_REGISTRY_EXCEPTION(20001, "注册失败"),
    /** 登录凭证缺失*/
    USER_NOT_LOGIN_EXCEPTION(20002, "用户未登录"),
    /** 商品创建失败 */
    PRODUCT_CREATE_EXCEPTION(30000, "商品创建失败"),
    /** 商品查询失败 */
    PRODUCT_NOT_EXIST_EXCEPTION(30001, "商品不存在"),
    /** 文件上传异常 */
    FILE_EXCEPTION(40000, "文件上传失败");
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
