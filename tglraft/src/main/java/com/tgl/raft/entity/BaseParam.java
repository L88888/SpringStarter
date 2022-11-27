package com.tgl.raft.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: spring-starter
 * @description: RPC协议消息发送的父类对象BaseParam
 * @author: LIULEI-TGL
 * @create: 2021-05-16 19:02:
 **/
@Setter
@Getter
public class BaseParam implements Serializable {

    /**
     * 候选人的任期号
     */
    public long term;

    /**
     * 被请求着的ID（ip:selfPort）
     * 自己的ip + 自己的端口(selfPort); 自己self
     */
    public String serviceId;
}
