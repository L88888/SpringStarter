package com.sailing.tgl.test.atomictest;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: spring-starter
 * @description: 使用jdk自带的JUC包，创建一个无锁函数实现自动生成全局唯一id,Long
 * @author: LIULEI
 * @create: 2021-05-15 16:35:
 **/
public class IdGenerator {

    AtomicLong atomicLong = new AtomicLong(0);

    public Long getId(){
        long resData = atomicLong.incrementAndGet();
        System.out.println("generator id>>>>" + resData);
        return resData;
    }

    @Test
    public void test(){
        // 必须加CountDownLatch
        int threadNum = 30000;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i =0;i< threadNum;i++){
            new Thread(()->{
                // 处理业务逻辑
                try {
                    long resData = atomicLong.incrementAndGet();
                    System.out.println("generator is value data:>" + resData);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 线程执行数量逐渐减少
                    countDownLatch.countDown();
                }
            }).start();
        }

        try {
            if(true){
                // 最大等待3秒钟时间，可以确保把所有的原子值AtomicLong生产完。
                countDownLatch.await(3000, TimeUnit.MILLISECONDS);
            }
            System.out.println("生成的原子数据，总量为:>" + atomicLong.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNodeNum(){
        int num = 3;
        System.out.println(num / 2);
    }
}
