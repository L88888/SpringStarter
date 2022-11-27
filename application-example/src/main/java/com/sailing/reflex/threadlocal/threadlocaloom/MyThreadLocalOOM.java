package com.sailing.reflex.threadlocal.threadlocaloom;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: spring-starter
 * @description: 测试ThreadLocal内存溢出
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-06-04 12:15:
 **/
public class MyThreadLocalOOM {

    // 声明500个线程
    private static final Integer SIZE = 500;

    // 创建一个线程池池，核心线程数量5个，线程阻塞队列LinkedBlockingQueue
    private static ThreadPoolExecutor executor =
            new ThreadPoolExecutor(5,5,1,
                    TimeUnit.MINUTES,
                    new LinkedBlockingQueue<>());

    /**
     * 定义一个测试对象Stu
     */
    static class Stu{
        // 创建一个5兆大小的byte数组，一会给threadlocal中做线程变量传递
        private byte[] localByte = new byte[1024 * 1024 * 5];
    }

    // 使用对象深拷贝来实现多线程之间对象共享的问题
    static ThreadLocal<Stu> tempData = new MyThreadLocal<>();
    public static void main(String[] ager){
        try {
            // 循环创建线程对象
            for (int i = 0;i < SIZE;i++){
                executor.execute(()->{
                    // 给每一个线程中添加Stu对象，给对象默认在内存中创建5兆空间
                    tempData.set(new Stu());
//                    tempData.set(null);
                    System.out.println(Thread.currentThread().getName() + "<:开始执行:>" + Thread.currentThread().getId());

                    // 处理内存泄露问题
                    tempData.remove();
                });

                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tempData = null;
    }
}