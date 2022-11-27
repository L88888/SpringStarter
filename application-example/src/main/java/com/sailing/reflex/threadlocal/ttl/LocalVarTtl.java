package com.sailing.reflex.threadlocal.ttl;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.sailing.reflex.threadlocal.bo.Stu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: spring-starter
 * @description: 测试提交给线程池的父线程变量，每次都可以被子线程读取到。
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-06-04 20:10:
 **/
public class LocalVarTtl {

    private ThreadLocal<Stu> ttlLocal = new MyThreadLocalTtl<>();

    // 先创建一个固定长度的线程池对象ExecutorService对象
    private ExecutorService executorService =
            TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(1));

    /**
     * 测试每次提交给线程池的子线程都可以读取到父线程的本地变量数据
     */
    private void test(){
        Stu stuData = new Stu("lisi","29","西安市户县");
        ttlLocal.set(stuData);

        // 提交一个线程任务
        executorService.submit(()->{
            System.out.println("读取主线程本地变量数据:>" + ttlLocal.get());
            ttlLocal.get().setSex("1234");
            System.out.println("读取主线程本地变量数据:>" + ttlLocal.get());

            ttlLocal.remove();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 修改主线程的属性数据
        ttlLocal.get().setSex("100");
        System.out.println("读取主线程本地变量数据,修改后变量数据:>" + ttlLocal.get());

        executorService.submit(()->{
            System.out.println("读取子线程本地变量数据:>" + ttlLocal.get());
            ttlLocal.get().setSex("3");
            System.out.println("读取子线程本地变量数据:>" + ttlLocal.get());

            ttlLocal.remove();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("读取主线程变量数据:>" + ttlLocal.get());
    }

    public static void main(String[] ager){
        LocalVarTtl t1 = new LocalVarTtl();
        t1.test();
    }
}
