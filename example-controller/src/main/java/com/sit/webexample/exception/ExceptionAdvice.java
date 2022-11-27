package com.sit.webexample.exception;

import com.sit.webexample.unifiedpackaging.SitResult;
import com.sit.webexample.unifiedpackaging.SitResultEnum;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice(basePackages = "com.sit.webexample")
public class ExceptionAdvice {

    /**
     * 捕获 {@code BusinessException} 异常
     */
    @ExceptionHandler({BusinessException.class})
    public SitResult<?> handleBusinessException(BusinessException ex) {
        return SitResult.failed(ex.getMessage());
    }

    /**
     * 捕获 {@code ForbiddenException} 异常
     */
    @ExceptionHandler({ForbiddenException.class})
    public SitResult<?> handleForbiddenException(ForbiddenException ex) {
        return SitResult.failed(SitResultEnum.FORBIDDEN);
    }

    /**
     * {@code @RequestBody} 参数校验不通过时抛出的异常处理
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public SitResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }

        String msg = sb.toString();
        if (StringUtils.hasText(msg)) {
            return SitResult.failed(SitResultEnum.VALIDATE_FAILED.getCode(), msg);
        }
        return SitResult.failed(SitResultEnum.VALIDATE_FAILED);
    }

    /**
     * {@code @PathVariable} 和 {@code @RequestParam} 参数校验不通过时抛出的异常处理
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public SitResult<?> handleConstraintViolationException(ConstraintViolationException ex) {
        if (StringUtils.hasText(ex.getMessage())) {
            return SitResult.failed(SitResultEnum.VALIDATE_FAILED.getCode(), ex.getMessage());
        }
        return SitResult.failed(SitResultEnum.VALIDATE_FAILED);
    }

    /**
     * 顶级异常捕获并统一处理，当其他异常无法处理时候选择使用
     */
    @ExceptionHandler({Exception.class})
    public SitResult<?> handle(Exception ex) {
        return SitResult.failed(ex.getMessage());
    }
}
