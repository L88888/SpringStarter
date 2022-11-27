package com.tgl.designpattern.exption;

/**
 * @program: spring-starter
 * @description: raft 一致性共识算法，远程通信异常
 * @author: LIULEI-TGL
 * @create: 2021-05-22 16:57:
 **/
public class RaftRemotingException extends RuntimeException {

    public RaftRemotingException(){}

    public RaftRemotingException(String message){
        super(message);
    }
}
