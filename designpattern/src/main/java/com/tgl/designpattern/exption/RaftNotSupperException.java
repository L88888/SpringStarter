package com.tgl.designpattern.exption;

/**
 * @program: spring-starter
 * @description: 一致性共识算法统一异常处理类
 * @author: LIULEI-TGL
 * @create: 2021-05-22 16:55:
 **/
public class RaftNotSupperException extends RuntimeException {

    public RaftNotSupperException(){}

    public RaftNotSupperException(String message){
        super(message);
    }
}
