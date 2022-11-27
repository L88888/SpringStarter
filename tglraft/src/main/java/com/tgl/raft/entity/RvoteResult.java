package com.tgl.raft.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: spring-starter
 * @description: 请求投票RPC返回对象接口
 * @author: LIULEI-TGL
 * @create: 2021-05-16 18:31:
 **/
@Setter
@Getter
@ToString
public class RvoteResult implements Serializable {

    /**
     * 当前任期号,以便于候选人去更新自己的任期
     */
    private long term;

    /**
     * 候选人赢得了此张选票时为真(true)
     */
    private boolean voteGranted;

    /**
     * 默认给对方投票
     * @param voteGranted true(成功)、false(选举失败)
     */
    public RvoteResult(boolean voteGranted){
        this.voteGranted = voteGranted;
    }

    public RvoteResult(Builder builder){
       setTerm(builder.term);
       setVoteGranted(builder.voteGranted);
    }

    /**
     * 选举失败
     * @return
     */
    public static RvoteResult fail(){
        return new RvoteResult(false);
    }

    /**
     * 选举成功
     * @return
     */
    public static RvoteResult ok(){
        return new RvoteResult(true);
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    @Override
    public String toString() {
        return "RvoteResult{" +
                "term=" + term +
                ", voteGranted=" + voteGranted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RvoteResult that = (RvoteResult) o;

        if (term != that.term) return false;
        return voteGranted == that.voteGranted;
    }

    @Override
    public int hashCode() {
        int result = (int) (term ^ (term >>> 32));
        result = 31 * result + (voteGranted ? 1 : 0);
        return result;
    }

    /**
     * RvoteResult对象编译Builder
     */
    public static final class Builder{
        private long term;
        private boolean voteGranted;

        public Builder(){}

        public Builder term(long term){
            this.term = term;
            return this;
        }

        public Builder voteGranted(boolean voteGranted){
            this.voteGranted = voteGranted;
            return this;
        }

        public RvoteResult build(){
            return new RvoteResult(this);
        }
    }
}
