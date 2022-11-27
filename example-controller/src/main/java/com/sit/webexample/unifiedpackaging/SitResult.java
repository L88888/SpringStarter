package com.sit.webexample.unifiedpackaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全系统统一返回数据结构对象SitResult
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SitResult<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> SitResult<T> success(T data) {
        return new SitResult<>(SitResultEnum.SUCCESS.getCode(), SitResultEnum.SUCCESS.getMessage(), data);
    }

    public static <T> SitResult<T> success(String message, T data) {
        return new SitResult<>(SitResultEnum.SUCCESS.getCode(), message, data);
    }

    public static SitResult<?> failed(String errorMsg) {
        return new SitResult<>(SitResultEnum.COMMON_FAILED.getCode(), errorMsg, null);
    }

    public static SitResult<?> failed() {
        return new SitResult<>(SitResultEnum.COMMON_FAILED.getCode(), SitResultEnum.COMMON_FAILED.getMessage(), null);
    }


    public static SitResult<?> failed(Integer code, String message) {
        return new SitResult<>(code, message, null);
    }

    public static SitResult<?> failed(ISitResult errorResult) {
        return new SitResult<>(errorResult.getCode(), errorResult.getMessage(), null);
    }

    public static <T> SitResult<T> instance(Integer code, String message, T data) {
        SitResult<T> result = new SitResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
