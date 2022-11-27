package com.sit.client.queue.producerqueue;

import com.google.common.collect.Queues;
import com.sit.client.device.entity.DeviceStatusDto;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class DeviceStatusQueue {

    /**
     * 模拟设备状态数据实时写入阻塞队列
     */
    private static final LinkedBlockingDeque<DeviceStatusDto> q =
            new LinkedBlockingDeque<>(500000);

    /**
     * 失败队列
     */
    private static final LinkedBlockingDeque<DeviceStatusDto> fail =
            new LinkedBlockingDeque<>(50000);
    /**
     * 存储队列数据
     * @return
     */
    public static void addData(DeviceStatusDto deviceStatusDto){
        try {
            q.push(deviceStatusDto);
        } catch (Exception e) {
            // 异常了之后把对应的数据对象放到fail队列中
            fail.push(deviceStatusDto);
            e.printStackTrace();
        }
    }

    /**
     * 获取队列数据
     * @return
     */
    public static List<DeviceStatusDto> getData(){
        try {
            List temp = new ArrayList<DeviceStatusDto>(5000);
            Queues.drain(q, temp, 10000, 1, TimeUnit.SECONDS);
            return temp;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        q.getFirst();

        return null;
    }
}
