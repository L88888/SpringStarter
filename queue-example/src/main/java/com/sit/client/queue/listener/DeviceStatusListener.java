//package com.sit.client.queue.listener;
//
//import com.sit.client.device.service.DeviceService;
//import com.sit.client.queue.event.DeviceStatusEvent;
//import com.sit.client.queue.producerqueue.DeviceStatusQueue;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Objects;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class DeviceStatusListener implements ApplicationListener<DeviceStatusEvent> {
//
//    private final DeviceService deviceService;
//
//    /**
//     * 使用onApplicationEvent方法消费事件数据
//     * @param event
//     */
//    @Override
//    public void onApplicationEvent(DeviceStatusEvent event) {
//        log.info("event data is value:=>{}", event);
//
//        DeviceStatusQueue.addData(event.getDeviceStatusDto());
//
//        List data = DeviceStatusQueue.getData();
//        if (Objects.isNull(data)){
//            log.info("queue data is null");
//            return;
//        }
//        deviceService.batchDeviceState(data);
//        data.clear();
//    }
//}
