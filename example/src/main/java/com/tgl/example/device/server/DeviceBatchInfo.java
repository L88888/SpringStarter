package com.tgl.example.device.server;

import com.tgl.example.device.entity.DeviceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @program: spring-starter
 * @description: 批量获取设备缓存对象
 * @author: LIULEI-TGL[知行合一]
 * @create: 2021-09-28 16:35:
 **/
@Service
public abstract class DeviceBatchInfo implements DeviceInfoCacheManager{

    @Autowired
    DeviceInfoCache deviceInfoCache;

    /**
     * 批量查询设备信息
     * @param deviceIds 设备ID
     * @return
     */
    @Override
    public DeviceEntity getBatchDeviceInfo(String[] deviceIds) {
        Assert.notNull(deviceIds, "设备编号集合");

        for (String deviceId : deviceIds){
            deviceInfoCache.getDeviceInfo(deviceId);
        }
        return null;
    }
}