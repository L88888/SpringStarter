package com.tgl.raftclient.client;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: spring-starter
 * @description: RPC客户端响应对象
 * @author: LIULEI-TGL
 * @create: 2021-05-20 18:08:
 **/
@Setter
@Getter
public class ClientKVAck implements Serializable {

    private Object result;

    public ClientKVAck(Object result){
        this.result = result;
    }

    public static ClientKVAck ok(){
        return new ClientKVAck("OK");
    }

    public static ClientKVAck fail(){
        return new ClientKVAck("FAIL");
    }

    public ClientKVAck(Builder builder){
        setResult(builder.result);
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    public static final class Builder{
        Object result;

        public Builder result(Object result){
            this.result = result;
            return this;
        }

        public ClientKVAck build(){
            return new ClientKVAck(this);
        }
    }
}
