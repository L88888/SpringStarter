package com.test.examples.dispeldoubts;

import org.junit.Test;

import java.util.concurrent.*;
import java.lang.String;

/**
 * @program: spring-starter
 * @description: 多线程结构
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-02-27 12:28:多线程结构
 **/
public class DispelDoubtsMultithreading {

    /**
     *  五种创建多线程的应用实例
     */
    public void m1(){
        // lambda 形式来创建多线程实例
        Thread thread = new Thread(()->{
            System.out.println("我是lambda！形式的线程.");
        });
        thread.start();
        System.out.println("m1通过lambda 形式来创建多线程实例.");
    }

    /**
     * 通过继承Thread对象的方式来创建线程
     */
    public void m2(){
        // 继承thread对象，实现run方法
        class MyThread extends Thread{
            @Override
            public void run(){
                System.out.println("启动线程对象MyThread()");
            }
        }

        // 启动线程对象MyThread对象()
        new MyThread().start();
        System.out.println("m2通过继承Thread对象来实现创建线程.");
    }

    /**
     * m3通过实现接口Runnable来创建线程
     */
    public void m3(){
        class MyRunnable implements Runnable{
            @Override
            public void run() {
                System.out.println("启动线程对象MyRunnable().");
            }
        }

        new Thread(new MyRunnable()).start();
        System.out.println("m3通过实现接口Runnable来创建线程.");
    }

    /**
     * 实现Calable接口，该接口可以收集线程执行后的返回值
     */
    class MyCallable implements Callable<String>{
        @Override
        public String call() throws Exception {
            System.out.println("启动线程对象MyCallable().");
            String resData = "Hello Callable！";
            Thread.sleep(3000);
            return resData;
        }
    }

    /**
     * 通过callable接口来实现带有返回值的线程实例
     */
    public void m4() throws Exception {
        Thread thread = new Thread(new FutureTask(new MyCallable()));
        thread.start();
        System.out.println("通过callable接口来实现带有返回值的线程实例");
    }

    /**
     * 通过线程池的方式启动线程对象
     */
    public void m5() throws ExecutionException, InterruptedException {
        // 创建线程池,来启动线程对象
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(()->{
            System.out.println("我是线程池对象创建的线程ABC.");
        });
        Future<String> future = executorService.submit(new MyCallable());
        System.out.println("阻塞线程,并获取线程执行结果对象:>" + future.get());
        executorService.shutdown();
        System.out.println("通过线程池的方式启动线程对象");
    }

    /**
     * 通过非线程池的方式来获取Callable线程的执行结果数据
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void m6() throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask(new MyCallable());
        FutureTask<String> bussTask = new FutureTask(new MyCallable());

        // 启动线程开始执行任务计划Task,todo 注意这块的线程启动与具体的逻辑顺序无关
        new Thread(bussTask).start();
        new Thread(futureTask).start();

        String resData = futureTask.get();
        System.out.println("通过非线程池的方式来获取Callable线程的" +
                "执行结果数据[resData]:>"+
                resData);

        String resDataTo = bussTask.get();
        System.out.println("通过非线程池的方式来获取Callable线程的" +
                "执行结果数据[resDataTo]:>"+
                resDataTo);
    }

    /**
     * 执行各种线程的启动方式
     */
    @Test
    public void execThread(){
        this.m1();
        this.m2();
        this.m3();
        try {
            this.m4();
            this.m5();
            this.m6();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 1秒后停止线程
     * volatile 用于处理共享变量数据,在多线程之间的相互可见
     */
    static volatile boolean ISRUN = false;

    private static void comperNum() throws InterruptedException {
        // 线程内部累加计数并将结果打印输出,一秒后停止计算并输出计算结果
        Thread t1 = new Thread(()->{
            long res = 0L;
            while (!ISRUN){
                res++;
            }
            System.out.println(res);
        });
        t1.start();

        // 1秒后停止线程运算
        Thread.sleep(500);
        ISRUN = true;
    }

    public static void main(String[] areg){
        try {
            comperNum();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}