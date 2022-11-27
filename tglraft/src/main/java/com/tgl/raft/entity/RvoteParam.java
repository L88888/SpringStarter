package com.tgl.raft.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: spring-starter
 * @description: 请求投票RPC参数对象
 * @author: LIULEI-TGL
 * @create: 2021-05-16 19:01:
 **/
@Setter
@Getter
@ToString
public class RvoteParam extends BaseParam {

    /**
     * 候选人的id，(ip:selfPort)ip:自己当前节点的端口
     */
    private String candidateId;

    /**
     * 候选人最后的日志条目下标索引
     */
    private long lastLogIndex;

    /**
     * 候选人最后的日志条目任期
     */
    private long lastLogTerm;

    public RvoteParam(Builder builder){
        setTerm(builder.term);
        setCandidateId(builder.candidateId);
        setLastLogIndex(builder.lastLogIndex);
        setLastLogTerm(builder.lastLogTerm);
        setServiceId(builder.serviceId);
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RvoteParam that = (RvoteParam) o;

        if (lastLogIndex != that.lastLogIndex) return false;
        if (lastLogTerm != that.lastLogTerm) return false;
        return candidateId != null ? candidateId.equals(that.candidateId) : that.candidateId == null;
    }

    @Override
    public int hashCode() {
        int result = candidateId != null ? candidateId.hashCode() : 0;
        result = 31 * result + (int) (lastLogIndex ^ (lastLogIndex >>> 32));
        result = 31 * result + (int) (lastLogTerm ^ (lastLogTerm >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "RvoteParam{" +
                "candidateId='" + candidateId + '\'' +
                ", lastLogIndex=" + lastLogIndex +
                ", lastLogTerm=" + lastLogTerm +
                '}';
    }

    public static final class Builder{
        String candidateId;
        long lastLogIndex;
        long lastLogTerm;
        long term;
        String serviceId;

        public Builder candidateId(String candidateId){
            this.candidateId = candidateId;
            return this;
        }

        public Builder lastLogIndex(long lastLogIndex){
            this.lastLogIndex = lastLogIndex;
            return this;
        }

        public Builder lastLogTerm(long lastLogTerm){
            this.lastLogTerm = lastLogTerm;
            return this;
        }

        public Builder term(long term){
            this.term= term;
            return this;
        }

        public Builder serviceId(String serviceId){
            this.serviceId = serviceId;
            return this;
        }

        public RvoteParam build(){
            return new RvoteParam(this);
        }
    }
}
