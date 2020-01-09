package org.study.error;

/**
 * @author fanqie
 * @date 2020/1/4
 */
public enum SystemExceptionBean implements SystemException {
    /** 参数异常 */
    PARAMETER_VALIDATION_EXCEPTION(10000, "参数不合法"),
    /** 加密信息异常 */
    ENCRYPT_DECRYPT_EXCEPTION(10001, "密钥异常"),
    /** 用户不存在 */
    USER_LOGIN_EXCEPTION(20000, "用户不存在或密码错误")
    ;

    private final Integer errCode;

    private final String errMsg;

    SystemExceptionBean(final Integer errCode, final String errMsg) {
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
