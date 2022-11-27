package com.sailing.tgl.test;

import com.tgl.raft.concurrent.TglRaftThreadHelper;
import com.tgl.raft.concurrent.TglRaftThreadPool;
import com.tgl.raft.entity.Command;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: spring-starter
 * @description: 守护线程、定时器测试验证
 * @author: LIULEI-TGL
 * @create: 2021-06-02 18:30:
 **/
@Slf4j
public class ThreadDomainTest {

    @Test
    public void testConCurrentJob(){
        log.info("守护线程开始执行");
        Election election = new Election();
        /**
         * 守护线程，延迟6秒后开始启动job任务，per 1秒执行一次任务调度
         */
        TglRaftThreadPool.scheduleAtFixdRate(election, 6000, 1000, TimeUnit.MILLISECONDS);

        // 主线程停止100秒
        TglRaftThreadHelper.sleep(100 * 1000);
        log.info("守护线程执行完毕");
    }

    @Test
    public void testExceptionJob(){
        ReentrantLock lock = new ReentrantLock();
        TglRaftThreadPool.scheduleAtFixdRate(()->{
            try {
                lock.tryLock(1000, TimeUnit.MILLISECONDS);
                System.out.println("============-------------------挂了");
                log.warn("不处理异常情况，业务逻辑会出现问题。");
                String s = "weq";
                Integer.parseInt(s);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        },0, 1,TimeUnit.MILLISECONDS);

        TglRaftThreadHelper.sleep(20 * 1000);
    }

    @Test
    public void testExecutor(){
        try {
            MyThreadTest001 myThreadTest001 = new MyThreadTest001();
            Future resData = TglRaftThreadPool.submit(myThreadTest001);
            if (false){
                log.info("{}", resData);
            }

            if (true){
                log.warn("接收执行结果:>{}",resData.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] agre){
        ThreadDomainTest threadDomain = new ThreadDomainTest();
        for (int i =0;i<10;i++){
            threadDomain.testExecutor();
        }
    }

    class MyThreadTest001 implements Callable{

        @Override
        public Object call() throws Exception {
            log.warn("我来处理话事人选举的整个事情。");
//            TglRaftThreadHelper.sleep(2 * 1000);
            return Command.newBuilder().key("hello").value("你好，吃了么！").build();
        }
    }

    /**
     * 选举线程任务，需要在任务执行过程中添加异常处理
     */
    class Election implements Runnable{

        @Override
        public void run() {
            try {
                Date date = new Date();
                System.out.println(date.toString() + "=============我是守护线程里头的逻辑处理.===========\n" +
                        "==================我会在后台默默执行，优先级很低=================\n" +
                        "==================我会随着主线程全部运行完而自动停止==============");

                String s = "asd";
                Integer.parseInt(s);
            } catch (Exception e) {
                e.printStackTrace();
                log.debug("业务逻辑处理异常，异常信息为:>{}", e.fillInStackTrace());
            }
        }
    }
}
