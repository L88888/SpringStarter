package com.tgl.raftclient.client;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: spring-starter
 * @description: RPC客户端请求对象,定义消息请求属性：type|key|value|,定义消息请求类型：GET|PUT
 * @author: LIULEI-TGL
 * @create: 2021-05-20 18:14:
 **/
@Setter
@Getter
public class ClientKVReq implements Serializable {

    public static final int PUT = 0;
    public static final int GET = 1;

    int type;
    String key;

    Object value;

    /**
     * 定义请求枚举类型put get
     */
    public enum ReqType{
        PUT(0),GET(1),INVALID(-1);

        public int code;
        ReqType(int code){
            this.code = code;
        }

        public static ReqType enumValue(int val){
            for (ReqType reqType : ReqType.values()){
                if (reqType.code == val){
                    return reqType;
                }
            }
            return INVALID;
        }
    }

    public ClientKVReq(Builder builder){
        setKey(builder.key);
        setType(builder.type);
        setValue(builder.value);
    }

    public ClientKVReq(){}

    public static Builder newBuilder(){
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientKVReq that = (ClientKVReq) o;

        if (type != that.type) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = type;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClientKVReq{" +
                "type=" + type +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public static final class Builder{
        int type;
        String key;
        Object value;

        public Builder type(int type){
            this.type = type;
            return this;
        }

        public Builder key(String key){
            this.key = key;
            return this;
        }

        public Builder value(Object value){
            this.value = value;
            return this;
        }

        public ClientKVReq build(){
            return new ClientKVReq(this);
        }
    }
}
