package com.sit.client.device.service;

import com.sit.client.device.entity.DeviceStatusDto;
import com.sit.client.queue.event.DeviceStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceSynchronization {

    /**
     * 使用构造函数注入,同步事件驱动
     */
    private final ApplicationContext applicationContext;

    /**
     * 异步事件驱动
     */
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * mock设备状态数据集合
     */
    public void startDeviceStatus(){
        new Thread(()->{
            try {
                // 模拟一秒上报100个设备的状态
                for (int i =0;i < 1000 * 200;i++){
                    if (i % 5000 == 0){
                        log.info("当前停顿下标为:=>{}", i);
                        Thread.sleep(100);
                    }
                    productDeviceStatus(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void productDeviceStatus(Integer i){
//        log.info("index data value:=>{}", i);
        DeviceStatusDto deviceStatusDto = new DeviceStatusDto();
        deviceStatusDto.setDeviceId("123123123" + i);
        deviceStatusDto.setStatus("1");
        deviceStatusDto.setWriteTime(
                String.valueOf(Integer.valueOf(String.valueOf(System.currentTimeMillis() / 1000))));
        deviceStatusDto.setDeviceName("科技路与丈八北路十字东向西:" + i);
        deviceStatusDto.setLatitude("102.1234567");
        deviceStatusDto.setLongitude("27.23456789");
        deviceStatusDto.setCameraType("1");
        deviceStatusDto.setDeviceArea("金泽派出所");
        deviceStatusDto.setCameraName("车辆,人脸,视频,人体");

//        applicationContext.publishEvent(new DeviceStatusEvent(this, deviceStatusDto));

        applicationEventPublisher.publishEvent(new DeviceStatusEvent(this, deviceStatusDto));
    }
}
