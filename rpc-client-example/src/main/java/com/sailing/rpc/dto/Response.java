package com.sailing.rpc.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: spring-starter
 * @description: 封装给客户端的任何结果对象
 * @author: LIULEI-TGL
 * @create: 2021-05-21 21:45:
 **/
@Setter
@Getter
public class Response<T> implements Serializable{

    private T result;

    private Response(){}

    public Response(T obj){
        this.result = obj;
    }

    public Response(Builder builder){
        setResult((T) builder.result);
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    public static final Response ok(){
        return new Response("ok");
    }

    public static final Response fail(){
        return new Response("fail");
    }

    public static final Response fail(String errorInfo){
        return new Response(errorInfo);
    }

    @Override
    public String toString() {
        return "Response{" +
                "result=" + result +
                '}';
    }

    public static final class Builder<T>{
        T result;

        public Builder result(T result){
            this.result = result;
            return this;
        }

        public Response build(){
            return new Response(this);
        }
    }
}