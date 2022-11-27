package com.sit.client.queue.handlerqueue;

import com.sit.client.device.service.DeviceService;
import com.sit.client.queue.producerqueue.DeviceStatusQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;

/**
 * 初始化任务服务
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    /**
     * 注入设备管理服务，进行设备状态实时入库操作
     */
    private final DeviceService deviceService;

    /**
     * 实时消费设备状态队列，将设备状态数写入pg中
     */
    @Async("dataEventExecutor")
    public void consumerDeviceStatusData(){
        while (true){
            try {
                // 需要启动另外一个线程单独进行数据获取与入库操作
                List data = DeviceStatusQueue.getData();
                log.info("data is size:=>{}", data.size());
                if (Objects.isNull(data) || data.isEmpty()){
                    log.info("queue data is null");
                    continue;
                }
                deviceService.batchDeviceState(data);
                data.clear();
            } catch (Exception e) {
                e.printStackTrace();
                log.info("数据批量入库异常,异常信息:=>{}", e.fillInStackTrace());
            }
        }
    }
}
