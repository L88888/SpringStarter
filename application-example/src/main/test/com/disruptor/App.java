package com.disruptor;

import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-03-08 11:40:
 **/
public class App {

    /**
     * Disruptor实际应用
     */
    public void app(){
        int bufferSize = 1024;

        // 创建Disruptor对象实例
        Disruptor<OrderEvent> disruptor = new
                Disruptor<>(OrderEvent::new,bufferSize, DaemonThreadFactory.INSTANCE);

        disruptor.handleEventsWith(App::handleOrderData);
        disruptor.handleEventsWith(App::handleOrderData1);
    }

    public static void handleOrderData(OrderEvent orderEvent, long seq, boolean endOfBatch){
        orderEvent.setId("111");
    }

    public static void handleOrderData1(OrderEvent orderEvent, long seq, boolean endOfBatch){
        orderEvent.setId("234567");
    }
}
