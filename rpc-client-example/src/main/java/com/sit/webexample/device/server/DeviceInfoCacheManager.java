package com.sit.webexample.device.server;

import com.sit.webexample.device.entity.DeviceEntity;

/**
 * @program: spring-starter
 * @description: 设备信息本地缓存管理
 * @author: LIULEI-TGL[知行合一]
 * @create: 2021-09-28 13:42:
 **/
public interface DeviceInfoCacheManager {

    /**
     * 删除缓存设备对象
     * @param deviceId 设备id
     * @return
     */
    public void delDeviceInfo(String deviceId);

    /**
     * 新增缓存设备对象
     * @param deviceId
     * @param deviceEntity
     * @return
     */
    public DeviceEntity putDeviceInfo(String deviceId, DeviceEntity deviceEntity);

    /**
     * 检索指定设备的缓存数据对象
     * @param deviceId 设备ID
     * @return DeviceEntity
     */
    public DeviceEntity getDeviceInfo(String deviceId);

    /**
     * 发布设备对象信息，到redis指定频道中。
     * 1、实现设备数据添加、更新的订阅/发布操作
     * @param deviceEntity
     */
    public void pubLishDeviceInfo(DeviceEntity deviceEntity);

    /**
     * 检索指定设备的缓存数据对象
     * @param deviceIds 设备ID
     * @return DeviceEntity
     */
    default public DeviceEntity getBatchDeviceInfo(String[] deviceIds){
        return null;
    }
}
