package com.sit.webexample.exception;

/**
 * 自定义运行时异常统一处理
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String msgData){
        super(msgData);
    }
}
