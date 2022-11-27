package com.tgl.binlogevent.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: spring-starter
 * @description: 监听线程执行的时间成本
 * @author: LIULEI-TGL
 * @create: 2021-05-23 17:47:
 **/
@Slf4j
public class TglRaftThreadPoolExecutor extends ThreadPoolExecutor {

    /** 计算每次线程执行的时间成本 */
    private static final ThreadLocal<Long> COST_TIME_WATCH = ThreadLocal.withInitial(System::currentTimeMillis);

    /**
     * 加入自定义线程池对象
     * @param corePoolSize 核心线程数
     * @param maximumPoolSize 多大线程数
     * @param keepAliveTime 保活时间
     * @param unit 时间单位
     * @param workQueue 线程池满后添加至队列中，等待下次消费执行
     * @param myTglThreadFactory 自定义线程池工厂
     */
    public TglRaftThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                     TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                     TglRaftThreadPool.MyTglThreadFactory myTglThreadFactory,
                                     RejectedExecutionHandler rejectedExecutionHandler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, myTglThreadFactory, rejectedExecutionHandler);
    }

    /**
     * 线程执行前触发
     * @param t
     * @param runnable
     */
    @Override
    public void beforeExecute(Thread t,Runnable runnable){
//        Long satrtTime = COST_TIME_WATCH.get();
//        log.info("Raft算法线程执行周期，前记录当前时间：>>>{}", COST_TIME_WATCH.get());
    }

    /**
     * 线程执行后触发
     * @param runnable
     * @param throwable
     */
    @Override
    public void afterExecute(Runnable runnable, Throwable throwable){
        try {
            // 计算线程执行耗时
//            log.info("Raft算法线程执行周期，当前线程:>>>{},执行耗时: >>>{}",
//                    Thread.currentThread().getName().concat(",线程ID:>") + Thread.currentThread().getId(),
//                    System.currentTimeMillis() - COST_TIME_WATCH.get());
        } finally {
            COST_TIME_WATCH.remove();
        }
    }

    /**
     * 线程执行结束, 统计输出激活的总数量、队列大小、线程池大小
     */
    @Override
    public void terminated() {
        log.info("运行中的线程总数:>{}, 队列大小:>{}, 线程池大小:>{}", getActiveCount(), getQueue().size(), getPoolSize());
    }
}
