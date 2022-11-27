package com.sailing.service.semaphore;

import java.util.concurrent.Semaphore;

/**
 * @program: spring-starter
 * @description: 信号量应用测试
 * @author: LIULEI
 * @create: 2021-03-02 09:13:
 **/
public class SemaphoreTemp {

    /**
     * 初始化信号量容量大小，默认10
     */
    private static Semaphore semaphore;

    public SemaphoreTemp(){
        semaphore = new Semaphore(10);
    }

    /**
     * 采用信号量来控制多线程的执行逻辑
     */
    public void carOIfTest(){
        for (int i = 0;i < 20; i++){
            try {
                Thread thread = new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("车辆" + Thread.currentThread().getName() + "来到A停车场.");
                            // 获取可用信号量
                            if (semaphore.availablePermits() == 0){
                                System.out.println("你好，车辆" + Thread.currentThread().getName() + ",A停车场车位已满，请等待.");
                            }

                            try {
                                // 获取可用的信号量
                                semaphore.acquire();
                                System.out.println("车辆" + Thread.currentThread().getName() + "进入A停车场.");
                                // 暂时没有可用的停车为，需要等待5秒
                                Thread.sleep(5000);
                                System.out.println("车辆" + Thread.currentThread().getName() + "使出A停车场.");
                                // 释放信号量
                                semaphore.release();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }, i+1 + "号车>>"
                );

                // 启动线程
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void carOIfTest(int flage){
        for (int i = 0;i < 20; i++){
            try {
                Thread thread = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    System.out.println("车辆" + Thread.currentThread().getName() + "来到A停车场.");
                                    // 获取可用信号量
                                    if (semaphore.availablePermits() > 0){
                                        semaphore.acquire();
                                        System.out.println("车辆" + Thread.currentThread().getName() + "进入A停车场.");

                                        // 模拟车辆使出A停车场
                                        Thread.sleep(5000);
                                        System.out.println("车辆" + Thread.currentThread().getName() + "使出A停车场.");
                                        semaphore.release();
                                    }else{
                                        System.out.println("你好，车辆" + Thread.currentThread().getName() + ",A停车场车位已满，请等待.");
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, i+1 + "号车>>"
                );

                // 启动线程
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        SemaphoreTemp semaphoreTemp = new SemaphoreTemp();
        semaphoreTemp.carOIfTest();
    }
}
