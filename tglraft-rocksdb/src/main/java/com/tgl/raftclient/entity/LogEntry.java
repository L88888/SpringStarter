package com.tgl.raftclient.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * @program: spring-starter
 * @description: 日志管理，日志条目对象。子对象Command
 * @author: LIULEI-TGL
 * @create: 2021-05-16 19:30:
 **/
@Setter
@Getter
public class LogEntry implements Serializable,Comparable {

    /**
     * 消息日志下标值
     */
    private Long index;

    /**
     * 候选人的任期，每参选一次自增1
     */
    private long term;

    /**
     * 日志消息子对象Command
     */
    private Command command;

    public LogEntry(){}

    public LogEntry(Builder builder){
        setIndex(builder.index);
        setTerm(builder.term);
        setCommand(builder.command);
    }

    /**
     * 候选人任期以及消息命令
     * @param term 投票者任期
     * @param command 消息命令
     */
    public LogEntry(long term, Command command){
        this.term = term;
        this.command = command;
    }

    /**
     * 候选人的日志下标，任期，消息命令
     * @param index 日志下标值，用来与当前节点上的日志下标进行比较
     * @param term 任期
     * @param command 消息命令
     */
    public LogEntry(Long index, long term, Command command){
        this.index = index;
        this.term = term;
        this.command = command;
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "index=" + index +
                ", term=" + term +
                ", command=" + command +
                '}';
    }

    /**
     * 候选人日志下标判断，用来对比候选人的日志跟自己节点的日志是不是一样的，如果一样的返回1，否则返回-1
     * @param o 候选人的日志对象LogEntry
     * @return
     */
    @Override
    public int compareTo(Object o) {
        if (o == null){
            return -1;
        }
        if (this.getIndex() > ((LogEntry)o).getIndex()){
            return 1;
        }
        return -1;
    }

    /**
     * 候选人的日志对象与当前节点的日志对象进行比较
     * @return
     */
    @Override
    public boolean equals(Object o){
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        LogEntry logEntry = (LogEntry) o;
        // 挨个属性值进行比较
        return this.term == logEntry.term && this.index == logEntry.index && this.command == logEntry.command;
    }

    /**
     * 重写对象的hashCode值
     * @return
     */
    @Override
    public int hashCode(){
        return Objects.hash(this.index, this.term, this.command);
    }

    public static final class Builder{
        private Long index;
        private long term;
        private Command command;

        public Builder index(Long index){
            this.index = index;
            return this;
        }

        public Builder term(long term){
            this.term = term;
            return this;
        }

        public Builder command(Command command){
            this.command = command;
            return this;
        }

        public LogEntry build(){
            return new LogEntry(this);
        }
    }
}
