package com.tgl.raftclient.concurrent;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: spring-starter
 * @description: raft 线程统一异常处理
 * @author: LIULEI-TGL
 * @create: 2021-05-23 17:39:
 **/
@Slf4j
public class TglRaftThread extends Thread {

    /** 处理线程统一异常处理 */
    public static final UncaughtExceptionHandler uncaughtExceptionHandler = (t, e)
            -> log.warn("Raft Exception occurred form thread {}, {}", t.getName(), e);

    public TglRaftThread(String thredName, Runnable runnable){
        super(runnable, thredName);
        setUncaughtExceptionHandler(uncaughtExceptionHandler);
    }
}
