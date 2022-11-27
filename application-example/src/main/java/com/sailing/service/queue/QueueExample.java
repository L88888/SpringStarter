package com.sailing.service.queue;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @program: spring-starter
 * @description: FIFO 非阻塞队列ConcurrentLinkedQueue，阻塞队列LinkedBlockingQueue
 * @author: LIULEI
 * @create: 2021-03-02 12:59:
 **/
public class QueueExample {

    /**
     * 设置队列大小
     */
    private static final int queueSize = 5000;

    /**
     * 非阻塞队列，适合单生产、多消费场景
     */
    private ConcurrentLinkedQueue dataSet;

    /**
     * 阻塞队列，适合多生产、单消费
     */
    private static LinkedBlockingQueue<PassCar> dataPassCar = new LinkedBlockingQueue<PassCar>(queueSize);

    /**
     * 初始化队列以及创建队列大小
     */
//    @PostConstruct
//    public void init(){
//        dataPassCar = new LinkedBlockingQueue<PassCar>(queueSize);
//    }

    /**
     * 获取当前队列大小值
     * @return
     */
    public static int getDataPassCarSize(){
        if (dataPassCar != null && dataPassCar.size() > 0){
            return dataPassCar.size();
        }
        return 0;
    }

    /**
     * 添加队列元素至队列末尾
     * @param passCar
     */
    public static void addDataPassCar(PassCar passCar){
        try {
            dataPassCar.put(passCar);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从队列顶端直接弹出队列元素对象
     * @return PassCar
     */
    public static PassCar pollDataPassCar(){
        return dataPassCar.poll();
    }

    /**
     * 从队列顶端获取队列元素对象，但是不直接弹出对象
     * @return PassCar
     */
    public static PassCar peekDataPassCar(){
        return dataPassCar.peek();
    }
}