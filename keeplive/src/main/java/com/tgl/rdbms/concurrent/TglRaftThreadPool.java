package com.tgl.rdbms.concurrent;

import java.util.concurrent.*;

/**
 * @program: spring-starter
 * @description: 自定义线程池对象
 * @author: LIULEI-TGL
 * @create: 2021-05-23 17:57:
 **/
public class TglRaftThreadPool {

    /** 多线程定时任务对象 */
    private static ScheduledExecutorService executorService = getScheduled();
    /** 多线程线程池对象 */
    private static ThreadPoolExecutor threadPoolExecutor = getThreadPool();

    /**
     * 获取线程池对象
     * @return
     */
    private static ThreadPoolExecutor getThreadPool(){
        return new TglRaftThreadPoolExecutor(TglRaftThreadHelper.cpu,
                TglRaftThreadHelper.maxPoolSize,
                TglRaftThreadHelper.keepTime,
                TglRaftThreadHelper.keepTimeUnit,
                new LinkedBlockingQueue<>(TglRaftThreadHelper.queueSize),
                new MyTglThreadFactory(),
                (runnable, executor)->{
                    // 自定义线程池拒绝策略，默认使用直接抛弃的方式
                    // 100ms询问一次线程池中是否有空闲，如果有就加进去执行
                    boolean isFree = false;
                    while (!isFree){
                        if (executor.getQueue().size() < executor.getMaximumPoolSize()){
                            isFree = executor.getQueue().add(runnable);
                        }
                        TglRaftThreadHelper.sleep(100);
                    }
                });
    }

    /**
     * 获取线程池调度执行的服务方法
     * @return
     */
    private static ScheduledExecutorService getScheduled(){
        return new ScheduledThreadPoolExecutor(TglRaftThreadHelper.cpu, new MyTglThreadFactory());
    }

    /**
     * 设置延迟任务执行时间间隔，通过period定义执行周期
     * @param taskInfo 需要执行的线程任务
     * @param initialDelay 初始延迟
     * @param period 时间
     * @param unit 单位
     */
    public static void scheduleAtFixdRate(Runnable taskInfo,
                                          long initialDelay,
                                          long period,
                                          TimeUnit unit){
        executorService.scheduleAtFixedRate(taskInfo, initialDelay, period, unit);
    }

    /**
     * 设置延迟任务执行时间间隔，通过delay定义执行周期
     * @param taskInfo 需要执行的现场任务
     * @param delay 时间
     * @param unit 单位
     */
    public static void scheduleWithFixedDelay(Runnable taskInfo,
                                              long delay,
                                              TimeUnit unit){
        executorService.scheduleWithFixedDelay(taskInfo, 0, delay, unit);
    }

    /**
     * 提交线程，开始执行
     * @param callable 需要执行的线程对象,可以得到返回值的线程对象
     * @return
     */
    public static Future submit(Callable callable){
        return threadPoolExecutor.submit(callable);
    }

    /**
     * 加入线程池对象中
     * @param runnable 需要执行的线程对象
     */
    public static void execute(Runnable runnable){
        threadPoolExecutor.execute(runnable);
    }

    /**
     * 线程任务是否需要排队执行
     * @param runnable 需要执行的线程任务对象
     * @param sync true 不需要排队直接执行当前线程，false 需要排队执行线程
     */
    public static void execute(Runnable runnable, boolean sync){
        if (sync){
            runnable.run();
        }else {
            threadPoolExecutor.execute(runnable);
        }
    }

    /**
     * 自定义线程工厂；
     * 1、定义守护线程
     * 2、定义线程执行优先级
     */
    public static class MyTglThreadFactory implements ThreadFactory{

        @Override
        public Thread newThread(Runnable r) {
            Thread raftThread = new TglRaftThread("Raft Thread TGL", r);
            // 设置运行的线程都是守护线程
            // todo 随着主线程的运行而在后台守护运行
            // todo 随着主线程退出而自动退出，他的优先级很低，但是他会一直运行。并不会影响用户线程的执行
            raftThread.setDaemon(true);
            // 自定义自己的线程名称
            raftThread.setName("TGL task-" + raftThread.getId());
            // 线程执行优先级
            raftThread.setPriority(5);
            return raftThread;
        }
    }
}
