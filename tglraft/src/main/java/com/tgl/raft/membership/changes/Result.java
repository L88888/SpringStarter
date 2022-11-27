package com.tgl.raft.membership.changes;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @program: spring-starter
 * @description: 集群节点新增与删除对象
 * @author: LIULEI-TGL
 * @create: 2021-05-20 13:05:
 **/
@Setter
@Getter
public class Result {

    // 状态
    private int status;
    // 领导提示
    private String leaderHint;

    @Getter
    public enum ResultStatusEnum{
        FAIL(0),SUCCESS(1),INVALID(-1),SBBH("设备编号"),ZZJG("组织机构");
        int codeVal;
        String enumCode;

        ResultStatusEnum(int code){
            this.codeVal = code;
        }

        ResultStatusEnum(String enumCode){
            this.enumCode = enumCode;
        }

        /**
         * 默认返回无效
         * @param val
         * @return
         */
        public static ResultStatusEnum enumValue(int val){
            for (ResultStatusEnum resultStatusEnum : ResultStatusEnum.values()){
                if (resultStatusEnum.codeVal == val){
                    return resultStatusEnum;
                }
            }
            return INVALID;
        }
    }

    public Result(Builder builder){
        setStatus(builder.status);
        setLeaderHint(builder.leaderHint);
    }

    public Result(){}

    public Result(int status, String leaderHint){
        this.status = status;
        this.leaderHint = leaderHint;
    }

    public static final Builder newBuilder(){
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        if (status != result.status) return false;
        return leaderHint != null ? leaderHint.equals(result.leaderHint) : result.leaderHint == null;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", leaderHint='" + leaderHint + '\'' +
                '}';
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.status, this.leaderHint);
    }

    public static final class Builder{
        int status;
        String leaderHint;

        public Builder status(int status){
            this.status = status;
            return this;
        }

        public Builder leaderHint(String leaderHint){
            this.leaderHint = leaderHint;
            return this;
        }

        public Result build(){
            return new Result(this);
        }
    }
}
