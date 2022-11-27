package com.tgl.raftclient.rpc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: spring-starter
 * @description: 客户端请求对象，封装客户端请求类型，以及具体请求动作; 主要完成请求投票、日志附加、客户端、配置变更（集群）add、配置变更（集群）remove
 * 请求类型
 * @author: LIULEI-TGL
 * @create: 2021-05-21 21:45:
 **/
@Setter
@Getter
@ToString
public class Request<T> implements Serializable {

    /** 请求投票 */
    public static final int R_VOTE = 0;

    /** 附加日志 */
    public static final int A_ENTRY = 1;

    /** 客户端请求 */
    public static final int CLIENT_REQ = 2;

    /** 配置更新添加 */
    public static final int CHANGE_CONFIG_ADD = 3;

    /** 配置更新删除 */
    public static final int CHANGE_CONFIG_REMOVE = 4;


    /** 请求类型 */
    private int cmd = -1;

    /** 数据对象
     *  附加日志 AentryParam
     *  请求投票 RvoteParam
     *  客户端请求对象 ClientKVAck
     * */
    private T obj;

    private String url;

    public Request(){}

    public Request(T obj){
        this.obj = obj;
    }

    public Request(int cmd, T obj,String url){
        this.obj = obj;
        this.cmd = cmd;
        this.url = url;
    }

    public Request(Builder builder){
        setCmd(builder.cmd);
        setObj((T)builder.obj);
        setUrl(builder.url);
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    public static final class Builder<T>{
        int cmd;
        T obj;
        String url;

        public Builder cmd(int cmd){
            this.cmd = cmd;
            return this;
        }

        public Builder obj(T obj){
            this.obj = obj;
            return this;
        }

        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Request build(){
            return new Request(this);
        }
    }
}
