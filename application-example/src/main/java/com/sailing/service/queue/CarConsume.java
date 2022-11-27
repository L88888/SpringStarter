package com.sailing.service.queue;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: spring-starter
 * @description: 从队列中直接消费车数据对象
 * @author: LIULEI
 * @create: 2021-03-02 15:45:
 **/
public class CarConsume {

    /**
     * 设置一个批次消费队列数据量大小
     */
    private final int queueSize = 5000;

    /**
     * 消费队列中的passcar对象数据
     */
    private void consumeQueueData(){
        try {
            List<PassCar> savePassCar = null;
            int passCarSize = 0;
            while (true){
                passCarSize = QueueExample.getDataPassCarSize();
                if (passCarSize >= queueSize){
                    System.out.println("批次满了，走批次提交.");
                    savePassCar = processPassCar(queueSize);
                    System.out.println("本批次消费队列数据长度为:>" + savePassCar.size() + ";队列长度为:>" + passCarSize);
//                    System.out.println("本批次消费队列数据为:>" + savePassCar);
                }else {
                    savePassCar = processPassCar(passCarSize);
                    if (passCarSize > 0){
                        System.out.println("批次没有满，走时间周期.");
                        System.out.println("本批次消费队列数据长度为:>" + savePassCar.size() + ";队列长度为:>" + passCarSize);
//                    System.out.println("本批次消费队列数据为:>" + savePassCar);
                    }
                }

                // 统一处理业务数据
//                System.out.println(savePassCar);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 统一处理队里中的数据
     * @param queueExample 自定义队列对象
     * @param size 队列大小
     * @return 车辆集合List
     */
    private List<PassCar> processPassCar(int size){
        List<PassCar> temp = new ArrayList<>();
        for (int i = 0;i < size;i++){
            temp.add(QueueExample.pollDataPassCar());
        }
        return temp;
    }

    /**
     * 启动线程来处理车辆队列数据
     */
    public void thredProcess(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                consumeQueueData();
            }
        }, "消费车辆队列数据-A001");
        thread.start();
    }
}
