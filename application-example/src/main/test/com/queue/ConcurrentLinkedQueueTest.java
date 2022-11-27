package com.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: spring-starter
 * @description: 阻塞型安全队列ConcurrentLinkQueue
 * @author: LIULEI
 * @create: 2021-04-21 16:42:
 **/
@Slf4j
public class ConcurrentLinkedQueueTest {

    /**
     * 阻塞型安全队列ConcurrentLinkQueue
     */
    public static ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();

    /** 队列总大小 */
    public final static int size1 = 1000000;

    /** 读写队列的线程池尺寸 */
    public final static int threadNumber = 10;

    /** 读队列的控制标识 */
    public static boolean isOver = false;

    public static void main(String[] args) throws InterruptedException,
            ExecutionException {
        ConcurrentLinkedQueueTest t1 = new ConcurrentLinkedQueueTest();
        t1.process();

        String t2 = "liulei";
        // 1102456712
        // 1102456712
        System.out.println("===>>" + t2.hashCode());
    }

    /**
     * 业务线程逻辑处理
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private void process() throws InterruptedException, ExecutionException{
        long timestart = System.currentTimeMillis();
        // 该线程开始给队列写进数据
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                ExecutorService executorService = Executors
                        .newFixedThreadPool(threadNumber,
                                new MyTglThreadFactory("Queue in"));
                ArrayList<Future<Long>> results = new ArrayList<Future<Long>>();
                for (int i = 0; i < threadNumber; i++) {
                    Future<Long> future = executorService.submit(new Exec());
                    results.add(future);
                }

//                CompletableFuture

                long allTime = 0;
                for (Future<Long> fs : results) {
                    try {
                        long singleInTime = fs.get();
                        allTime += singleInTime;
                        log.info("单个线程入队列用时:>{}", singleInTime);
                    } catch (InterruptedException e) {
                        log.info("" + e);
                        return;
                    } catch (ExecutionException e) {
                        log.info("" + e);
                    } finally {
                        // 停止线程池
                        executorService.shutdown();
                    }
                }

                // 数据全部写完之后,即刻通知另外一个线程把消费队列的读取控制状态修改为true
                ConcurrentLinkedQueueTest.isOver = true;
                log.info("总线程入队列总共执行时间：" + allTime);
            }
        },"数据写队列-" + Thread.currentThread().getId());
        thread1.start();

        // 该线程开始从队列弹出数据
        MyTglThreadFactory myTglThreadFactory = new MyTglThreadFactory("数据出队列-");
        myTglThreadFactory.newThread(new Runnable() {
            public void run() {
                ExecutorService executorService2 = Executors
                        .newFixedThreadPool(threadNumber,
                                new MyTglThreadFactory("Queue out"));
                ArrayList<Future<Long>> results_out = new ArrayList<Future<Long>>();
                for (int i = 0; i < threadNumber; i++) {
                    log.info("开始弹出队列数据对象.");
                    Future<Long> future = executorService2.submit(new Exec_Out());
                    results_out.add(future);
                }

                long allTime_out = 0;
                for (Future<Long> fs : results_out) {
                    try {
                        long singleOutTime = fs.get();
                        allTime_out += singleOutTime;
                        log.info("单个线程出队列用时:>{}", singleOutTime);
                    } catch (InterruptedException e) {
                        log.info("" + e);
                        return;
                    } catch (ExecutionException e) {
                        log.info("" + e);
                    } finally {
                        // 停止线程池
                        executorService2.shutdown();
                    }
                }
                log.info("总线程出队列总共执行时间：" + allTime_out);
            }
        }).start();

        log.info("主线程执行时间：" + (System.currentTimeMillis() - timestart));
    }
}

/**
 * 多线程给队列写数据
 */
@Slf4j
class Exec implements Callable<Long> {

    @Override
    public Long call() throws Exception {
        long time = System.currentTimeMillis();

        for (int i = 0; i < ConcurrentLinkedQueueTest.size1; i++) {
            ConcurrentLinkedQueueTest.queue.offer(i);
        }

        long time2 = System.currentTimeMillis() - time;
        log.info("进队列用时：" + time2);
        return time2;
    }
}

/**
 * 多线程从队列读数据
 */
@Slf4j
class Exec_Out implements Callable<Long> {

    @Override
    public Long call() throws Exception {
        long time = System.currentTimeMillis();
        while (!ConcurrentLinkedQueueTest.isOver) {
            ConcurrentLinkedQueueTest.queue.poll();
        }
        long time2 = System.currentTimeMillis() - time;
        log.info("出队列用时：" + time2);
        return time2;
    }
}

class MyTglThreadFactory implements ThreadFactory{

    private final String threadName;
    // 记录线程ID号
    private final AtomicInteger num = new AtomicInteger(1);

    public MyTglThreadFactory(String threadName){
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread raftThread =
                new Thread(null, r, threadName + "-" + num.getAndIncrement(),0);
        // 设置运行的线程都是守护线程
        // todo 随着主线程的运行而在后台守护运行
        // todo 随着主线程退出而自动退出，他的优先级很低，但是他会一直运行。并不会影响用户线程的执行
        raftThread.setDaemon(true);
        // 线程执行优先级
        raftThread.setPriority(5);
        return raftThread;
    }
}
