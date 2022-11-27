package com.sailing.service.queue;

import java.util.Date;

/**
 * @program: spring-starter
 * @description: 车辆数据入队列
 * @author: LIULEI
 * @create: 2021-03-02 15:38:
 **/
public class CarProduction {

    /**
     * 车辆数据入队列
     */
    private void carSaveQueueOne(){
        // 模拟1万车数据量
        try {
            for (int i =0;i < 30000;i++){
                PassCar passCar = new PassCar();
                passCar.setCid("vehicle" + i);
                passCar.setCarName("vehicle" + i);
                passCar.setPassTime(new Date().toString());
                passCar.setPlanNo("vehicle" + i);

                if (i % 5000 == 0){
                    Thread.sleep(10000);
                }

                if (i == 20000){
                    Thread.sleep(30000);
                }

                QueueExample.addDataPassCar(passCar);
            }

            System.out.println("车数据-A002已经生产完成.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void carSaveQueueTo(){
        // 模拟1万车数据量
        try {
            for (int i =0;i < 30000;i++){
                PassCar passCar = new PassCar();
                passCar.setCid("vehicle" + i);
                passCar.setCarName("vehicle" + i);
                passCar.setPassTime(new Date().toString());
                passCar.setPlanNo("vehicle" + i);

                if (i % 5000 == 0){
                    Thread.sleep(10000);
                }

                if (i == 20000){
                    Thread.sleep(30000);
                }

                QueueExample.addDataPassCar(passCar);
            }

            System.out.println("车数据-A003已经生产完成.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void thredProcess(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                carSaveQueueOne();
            }
        },"生产车辆队列数据-A002");
        thread.start();

        Thread threadto = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                carSaveQueueTo();
            }
        },"生产车辆队列数据-A003");
        threadto.start();
    }
}
