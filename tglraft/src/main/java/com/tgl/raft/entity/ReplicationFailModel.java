package com.tgl.raft.entity;

import com.tgl.raft.cluster.Peer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * @program: spring-starter
 * @description: 失败的复本模型，消息处理失败后需要放到队列中等待下次继续处理
 * @author: LIULEI-TGL
 * @create: 2021-05-25 13:39:
 **/
@Setter
@Getter
public class ReplicationFailModel implements Serializable {

    public static String count = "_count";
    public static String success = "_success";

    private LogEntry logEntry;
    private Peer peer;
    private Long offerTime;
    private String countKey;
    private String successKey;
    private Callable callable;

    private ReplicationFailModel(){}

    public ReplicationFailModel(Builder builder){
        setCallable(builder.callable);
        setCountKey(builder.countKey);
        setLogEntry(builder.logEntry);
        setOfferTime(builder.offerTime);
        setPeer(builder.peer);
        setSuccessKey(builder.successKey);
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    public static final class Builder{
        public LogEntry logEntry;
        public Peer peer;
        public Long offerTime;
        public String countKey;
        public String successKey;
        public Callable callable;

        public Builder logEntry(LogEntry logEntry){
            this.logEntry = logEntry;
            return this;
        }

        public Builder peer(Peer peer){
            this.peer = peer;
            return this;
        }

        public Builder offerTime(Long offerTime){
            this.offerTime = offerTime;
            return this;
        }

        public Builder countKey(String countKey){
            this.countKey = countKey;
            return this;
        }

        public Builder successKey(String successKey){
            this.successKey = successKey;
            return this;
        }

        public Builder callable(Callable callable){
            this.callable = callable;
            return this;
        }

        public ReplicationFailModel build(){
            return new ReplicationFailModel(this);
        }
    }
}
