package com.sailing.T20221127.threadsync;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName MyThread
 * @Description 测试多线程模式下synchronization的锁范围
 * @Author Liulei
 * @Date 2022/11/27 15:20
 * @Version 1.0
 **/
@Slf4j
public class MyThread extends Thread{

    // 创建ThreadSyncUse对象的引用threadSyncUse
    private ThreadSyncUse threadSyncUse;

    public MyThread(){}

    /**
     * 自定义线程名称以及线程锁对象ThreadSyncUse
     * @param threadSyncUse
     */
    public MyThread(String threadName, ThreadSyncUse threadSyncUse){
        this.threadSyncUse = threadSyncUse;
        this.setName(threadName);
    }

    @Override
    public void run(){
        if (this.getName().equals("Thread1")){
            // 执行function1方法
            threadSyncUse.function1(this);
        }else {
            log.info("Thread2 启动，等待进入同步方法function2");
            threadSyncUse.function2(this);
        }
    }
}
