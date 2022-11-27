package com.sailing.service.queue;

/**
 * @program: spring-starter
 * @description: 测试队列LinkedBlockingQueue生产者与消费者
 * @author: LIULEI
 * @create: 2021-03-02 16:10:
 **/
public class PassCarTest {
    public static void main(String[] args){
        // 生产者
        CarProduction carProduction = new CarProduction();
        carProduction.thredProcess();

        // 消费者
        CarConsume carConsume = new CarConsume();
        carConsume.thredProcess();
    }
}
