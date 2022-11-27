package com.sit.client.queue.event;

import com.google.common.collect.Queues;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @program: spring-starter
 * @description: 套牌车中间对象
 * @author: LIULEI-TGL
 * @create: 2021-08-31 16:32:
 **/
@Slf4j
public class TpcEvent {

    /**
     * 模拟设备状态数据实时写入阻塞队列
     */
    LinkedBlockingDeque<Object> q = new LinkedBlockingDeque<Object>(50000);

    /**
     * 每隔一段时间获取一次队列中的数据
     */
    public void realGetData(){
        try {
            List temp = null;
            while(true){
                temp = new ArrayList<>(1000);
                log.info("开始处理消费的数据");
                Queues.drain(q, temp, 500, 5, TimeUnit.SECONDS);
                log.info("一个批次的数处理完成了，容器大小为:>{}", temp.size());

                temp.clear();
                temp = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 消费队列设备状态数据开始入库pg库
        System.out.println("aaaaaaaaaaaa");
    }

    /**
     * 一秒给队列新增100条数据
     */
    private void addData(){
        if (Objects.isNull(q)){
            return;
        }

        try {
            while(true){
//                System.out.println("开始添加业务数据");
                q.add("qweq");
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] ager){
        TpcEvent event = new TpcEvent();
        new Thread(()->{
            event.realGetData();
        }).start();

        new Thread(()->{
            event.addData();
        }).start();
    }
}
