package com.sailing.tgl.test;

import com.tgl.raft.entity.LogEntry;
import org.junit.Test;

import java.util.LinkedList;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-06-03 15:18:
 **/
public class LogReplication {

    @Test
    public void replicationLog(){

        // 顺序存储，顺序遍历读取
        LinkedList<LogEntry> logEntries = new LinkedList<>();

        LogEntry logEntry = LogEntry.newBuilder().index(1L).term(1).command(null).build();
        logEntries.add(logEntry);

        LogEntry logEntry1 = LogEntry.newBuilder().index(2L).term(1).command(null).build();
        logEntries.add(logEntry1);

        LogEntry logEntry2 = LogEntry.newBuilder().index(3L).term(1).command(null).build();
        logEntries.add(logEntry2);

        LogEntry logEntry3 = LogEntry.newBuilder().index(4L).term(1).command(null).build();
        logEntries.add(logEntry3);

        System.out.println(logEntries.toArray(new LogEntry[0]).length);

        System.out.println(logEntries.toArray().length);

        for (Object o : logEntries.toArray()){
            System.out.println("不带反射对象，需要强转:>" + o.toString());
        }

        for (LogEntry logEntrys : logEntries.toArray(new LogEntry[0])){
            System.out.println("自身就是反射后的对象，不需要强转:>" + logEntrys);
        }
    }
}
