package com.sit.webexample.exception;

/**
 * 自定义业务运行是异常
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String msgData){
        super(msgData);
    }
}
