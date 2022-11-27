package com.sailing.T20221127.threadsync;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ThreadSyncUse
 * @Description 多线程同步的使用方式 synchronization(同步锁)
 * @Author Liulei
 * @Date 2022/11/27 15:10
 * @Version 1.0
 **/
@Slf4j
public class ThreadSyncUse {

    // 测试synchronization在方法上加，跟方法里头加锁的范围

    /**
     * 使用synchronization 锁住方法function1()
     * @param currentThread
     */
//    public synchronized void function1(Thread currentThread){
//        log.info("线程执行function1方法，当前线程名称:>{}", currentThread.getName() + currentThread.getId());
//
//        try {
//            Thread.sleep(1000L);
//        } catch (InterruptedException e) {
//            log.debug("当前函数执行异常，异常信息为:>{}", e);
//        }
//    }

    /**
     * synchronized 方法内部锁this其实也是锁的当前对象
     * @param currentThread
     */
    public void function1(Thread currentThread){
        log.info("线程执行function1方法，当前线程名称:>{}", currentThread.getName() + currentThread.getId());
        synchronized(this) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                log.debug("当前函数执行异常，异常信息为:>{}", e);
            }
        }
    }

    /**
     * synchronized 锁加在方法上锁的是当前整个对象。
     * 程序执行时需要等到具体被锁方法执行完成后才能开始执行其他同步方法
     * @param currentThread
     */
    public synchronized void function2(Thread currentThread){
        log.info("线程执行function2方法，当前线程名称:>{}", currentThread.getName() + currentThread.getId());
    }
}
