package com.tgl.raft.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: spring-starter
 * @description: 节点日志更新结果对象
 * @author: LIULEI-TGL
 * @create: 2021-05-16 18:45:
 **/
@Setter
@Getter
@ToString
public class AentryResult implements Serializable {

    /**
     * 当前任期号,以便于候选人去更新自己的任期
     */
    private long term;

    /**
     * 跟随者包含了: prevLogIndex 和 preLogTerm 的日志时为真
     */
    private boolean success;

    /** 失败信息 */
    private String failInfo;

    public AentryResult(long term, boolean success){
        this.term = term;
        this.success = success;
    }

    public AentryResult(boolean success){
        this.success = success;
    }

    public AentryResult(Builder builder){
        setTerm(builder.term);
        setSuccess(builder.success);
    }

    /**
     * 更新日志失败
     * @return
     */
    public static AentryResult fail(){
        return new AentryResult(false);
    }

    /**
     * 更新日志成功
     * @return
     */
    public static AentryResult ok(){
        return new AentryResult(true);
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    @Override
    public String toString() {
        return "AentryResult{" +
                "term=" + term +
                ", success=" + success +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AentryResult that = (AentryResult) o;

        if (term != that.term) return false;
        return success == that.success;
    }

    @Override
    public int hashCode() {
        int result = (int) (term ^ (term >>> 32));
        result = 31 * result + (success ? 1 : 0);
        return result;
    }

    /**
     * AentryResult对象编译Builder
     */
    public static final class Builder{
        long term;
        boolean success;

        public Builder(){}

        public Builder term(long term){
            this.term = term;
            return this;
        }

        public Builder success(boolean success){
            this.success = success;
            return this;
        }

        public AentryResult build(){
            return new AentryResult(this);
        }
    }
}
