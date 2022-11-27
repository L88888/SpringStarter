package com.sit.client.queue.listener;

import com.sit.client.queue.event.DeviceStatusEvent;
import com.sit.client.queue.producerqueue.DeviceStatusQueue;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeviceStatusAsyncListener{

    /**
     * 使用onApplicationEvent方法消费事件数据
     * @param event
     */
    @SneakyThrows
    @EventListener(DeviceStatusEvent.class)
    public void onApplicationEvent(DeviceStatusEvent event) {
//        log.info("event receive data is value:=>{}", event);

        DeviceStatusQueue.addData(event.getDeviceStatusDto());
    }
}
