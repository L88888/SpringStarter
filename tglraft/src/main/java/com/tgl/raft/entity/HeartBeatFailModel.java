package com.tgl.raft.entity;

import com.tgl.raft.cluster.Peer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * @program: spring-starter
 * @description: 心跳发送失败需要放到队列中重新发送
 * @author: LIULEI-TGL
 * @create: 2021-06-03 21:28:
 **/
@Setter
@Getter
public class HeartBeatFailModel implements Serializable {

    private Peer peer;
    private Long officeTime;
    private Callable callable;
    private AentryParam aentryParam;

    public HeartBeatFailModel(Builder builder){
        setAentryParam(builder.aentryParam);
        setCallable(builder.callable);
        setOfficeTime(builder.officeTime);
        setPeer(builder.peer);
    }

    public static HeartBeatFailModel newBuilder(){
        return new Builder().build();
    }

    static class Builder{
        private Peer peer;
        private Long officeTime;
        private Callable callable;
        private AentryParam aentryParam;

        public Builder peer(Peer peer){
            this.peer = peer;
            return this;
        }

        public Builder officeTime(Long officeTime){
            this.officeTime = officeTime;
            return this;
        }

        public Builder callable(Callable callable){
            this.callable = callable;
            return this;
        }

        public Builder aentryParam(AentryParam aentryParam){
            this.aentryParam = aentryParam;
            return this;
        }

        public HeartBeatFailModel build(){
            return new HeartBeatFailModel(this);
        }
    }
}
