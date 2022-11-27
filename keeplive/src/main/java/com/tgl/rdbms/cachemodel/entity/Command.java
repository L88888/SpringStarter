package com.tgl.rdbms.cachemodel.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: spring-starter
 * @description: 状态机命令对象，用来存储日志消息的载体
 * @author: LIULEI-TGL
 * @create: 2021-05-16 19:31:
 **/
@Setter
@Getter
@ToString
public class Command implements Serializable {

    private String key;
    private Object value;

//    public Command(Builder builder){
//        setKey(builder.key);
//        setValue(builder.value);
//    }

    public Command(){}

    public Command(String key, Object value){
        this.key = key;
        this.value = value;
    }

//    /**
//     * 重写Command命令对象hash值，保证对象hash值不冲突
//     * @return
//     */
//    @Override
//    public int hashCode(){
//        return Objects.hash(this.key, this.value);
//    }
//
//    /**
//     * 重写对象的比对策略
//     * @param o 待比较的对象
//     * @return
//     */
//    @Override
//    public boolean equals(Object o){
//        if (this == o){
//            return true;
//        }
//
//        if (o == null || getClass() != o.getClass()){
//            return false;
//        }
//        Command  command = (Command)o;
//        return command.key == this.key && command.value == this.value;
//    }
//
//    public static Builder newBuilder(){
//        return new Builder();
//    }

    @Override
    public String toString() {
        return "Command{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

//    public static final class Builder{
//        String key;
//        Object value;
//
//        public Builder key(String key){
//            this.key = key;
//            return this;
//        }
//
//        public Builder value(Object value){
//            this.value = value;
//            return this;
//        }
//
//        public Command build(){
//            return new Command(this);
//        }
//    }
}
