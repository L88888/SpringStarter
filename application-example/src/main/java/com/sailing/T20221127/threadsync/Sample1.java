package com.sailing.T20221127.threadsync;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @ClassName Sample1
 * @Description 主测试类
 * @Author Liulei
 * @Date 2022/11/27 15:30
 * @Version 1.0
 **/
@Slf4j
public class Sample1 {

//    @Test
    public static void main(String[] are){
        ThreadSyncUse threadSyncUse = new ThreadSyncUse();
        MyThread t1 = new MyThread("Thread1", threadSyncUse);
        MyThread t2 = new MyThread("Thread2", threadSyncUse);

        log.info("T1 线程开始启动.");
        t1.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("T2 线程开始启动.");
        t2.start();
    }
}
