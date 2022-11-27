package com.sit.webexample.unifiedpackaging;

/**
 * 全系统自定义常用结果的枚举字典
 */
public enum SitResultEnum implements ISitResult {

    SUCCESS(3005, "接口调用成功"),
    VALIDATE_FAILED(3006, "参数校验失败"),
    COMMON_FAILED(3007, "接口调用失败"),
    FORBIDDEN(3008, "没有权限访问资源");

    private Integer code;
    private String message;

    SitResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
