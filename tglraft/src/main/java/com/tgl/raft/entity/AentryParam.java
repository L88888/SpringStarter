package com.tgl.raft.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;

/**
 * @program: spring-starter
 * @description: 附加日志RCP参数对象. handlerAppendEntries
 * @author: LIULEI-TGL
 * @create: 2021-05-16 19:06:
 **/
@Setter
@Getter
@ToString
public class AentryParam extends BaseParam {

    /**
     * 领导人id，以便于跟随者重定向请求
     */
    private String leaderId;

    /**
     * 新的日志条目紧随之前的日志索引值
     */
    private long prevLogIndex;

    /**
     * 日志条目的任期号
     */
    private long prevLogTerm;

    /**
     * 领导人已经提交的日志索引值
     */
    private long leaderCommit;

    /**
     * 日志条目集合，一次可以发送多个日志为了提高消息效率。发送心跳包时该值为空
     */
    private LogEntry[] logEntrys;

    /**
     * 专门用来发送心跳包,{keepLive:true}表示成功
     */
    private boolean keepLive;

    public AentryParam(Builder builder){
        setLeaderId(builder.leaderId);
        setKeepLive(builder.keepLive);
        setLeaderCommit(builder.leaderCommit);
        setLogEntrys(builder.logEntrys);
        setServiceId(builder.serviceId);
        setTerm(builder.term);
        setPrevLogIndex(builder.prevLogIndex);
        setPrevLogTerm(builder.prevLogTerm);
    }

    public AentryParam(){}

    public static Builder newBuilder(){
        return new Builder();
    }

    @Override
    public String toString() {
        return "AentryParam{" +
                "leaderId='" + leaderId + '\'' +
                ", prevLogIndex=" + prevLogIndex +
                ", prevLogTerm=" + prevLogTerm +
                ", leaderCommit=" + leaderCommit +
                ", logEntrys=" + Arrays.toString(logEntrys) +
                ", keepLive=" + keepLive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AentryParam that = (AentryParam) o;

        if (prevLogIndex != that.prevLogIndex) return false;
        if (prevLogTerm != that.prevLogTerm) return false;
        if (leaderCommit != that.leaderCommit) return false;
        if (keepLive != that.keepLive) return false;
        if (leaderId != null ? !leaderId.equals(that.leaderId) : that.leaderId != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(logEntrys, that.logEntrys);
    }

    @Override
    public int hashCode() {
        int result = leaderId != null ? leaderId.hashCode() : 0;
        result = 31 * result + (int) (prevLogIndex ^ (prevLogIndex >>> 32));
        result = 31 * result + (int) (prevLogTerm ^ (prevLogTerm >>> 32));
        result = 31 * result + (int) (leaderCommit ^ (leaderCommit >>> 32));
        result = 31 * result + Arrays.hashCode(logEntrys);
        result = 31 * result + (keepLive ? 1 : 0);
        return result;
    }

    public static final class Builder{
        String leaderId;
        long prevLogIndex;
        long prevLogTerm;
        long leaderCommit;
        LogEntry[] logEntrys;
        boolean keepLive;
        long term;
        String serviceId;

        public Builder leaderId(String leaderId){
            this.leaderId = leaderId;
            return this;
        }

        public Builder prevLogIndex(long prevLogIndex){
            this.prevLogIndex = prevLogIndex;
            return this;
        }

        public Builder prevLogTerm(long prevLogTerm){
            this.prevLogTerm = prevLogTerm;
            return this;
        }

        public Builder leaderCommit(long leaderCommit){
            this.leaderCommit = leaderCommit;
            return this;
        }

        public Builder term(long term){
            this.term = term;
            return this;
        }

        public Builder serviceId(String serviceId){
            this.serviceId = serviceId;
            return this;
        }

        public Builder keepLive(boolean keepLive){
            this.keepLive = keepLive;
            return this;
        }

        public Builder logEntrys(LogEntry[] logEntrys){
            this.logEntrys = logEntrys;
            return this;
        }

        public AentryParam build(){
            return new AentryParam(this);
        }
    }
}
