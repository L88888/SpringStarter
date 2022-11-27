package com.sit.client.device.service;

import com.sit.client.device.batch.BatchUtils;
import com.sit.client.device.entity.DeviceStatusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class DeviceService {

    @Autowired
    private BatchUtils batchUtils;

    /**
     * 模拟10万条测试数据批量入库
     */
    public void batchDeviceState(List<DeviceStatusDto> data) {
        log.info("开始批量入库:=>{}.", data.size());
        batchUtils.batchUpdateOrInsert(data, DbSyncMapper.class,
                (item, DbSyncMapper) -> DbSyncMapper.insertDeviceStatusTemp(item));
        log.info("批量入库结束.");
    }
}
