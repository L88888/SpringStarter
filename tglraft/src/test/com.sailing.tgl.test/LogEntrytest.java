package com.sailing.tgl.test;

import com.tgl.raft.core.LogModule;
import com.tgl.raft.entity.Command;
import com.tgl.raft.entity.LogEntry;
import com.tgl.raft.impl.DefaultLogEntryImpl;
import org.junit.Test;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-05-31 14:37:
 **/
public class LogEntrytest {

    @Test
    public void testLogEntry(){
        System.setProperty("clientPort","1314");
        LogModule logModule = DefaultLogEntryImpl.getInstance();
        String bussKey = "test00808";
        Command command = Command.newBuilder()
                .key(bussKey)
                .value("你好，世界！现场问题跟进处理。")
                .build();

        LogEntry logEntry = LogEntry.newBuilder()
                .term(12)
                .index(-90L)
                .command(command)
                .build();

        for (int i =0;i< 10;i++){
        }

        System.out.println("最大的下标值为:>>>{}" +  logModule.getLastIndex());

        LogEntry logEntry1Last = logModule.getLast();
        System.out.println("获取最新的日志对象Last:>>>{}" +  logEntry1Last);

        System.out.println("待写入对象:>>>{}" +  logEntry);
//        logModule.write(logEntry);

//        logModule.removeOnStartIndex(0L);
        LogEntry resData = logModule.read(logModule.getLastIndex() - 1000);
        System.out.println("获取日志对象:>>>{}" +  resData);

        LogEntry logEntry1 = logModule.getLast();
        System.out.println("获取最新的日志对象:>>>{}" +  logEntry1);
    }

    /**
     * 测试日志条目logentry
     */
    @Test
    public void getLogEntry(){
        LogModule logModule = DefaultLogEntryImpl.getInstance();
//        logModule.removeOnStartIndex(0L);

        System.out.println(">>>{}" + logModule.getLastIndex());
        // 遍历日志条目数据,挨个获取日志中的车辆保有量数据
        LogEntry logEntry = null;
        for (long i = 0 ;i<=logModule.getLastIndex();i++){
            logEntry = logModule.read(i);
            System.out.println("车辆保有量数据:>{}" + logEntry);
        }
    }
}
