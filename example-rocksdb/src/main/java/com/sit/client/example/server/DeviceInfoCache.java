package com.sit.client.example.server;

import com.alibaba.fastjson.JSON;
import com.sit.client.cacheconfig.redis.RedisUtil;
import com.sit.client.example.entity.DeviceEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @program: spring-starter
 * @description: 使用二级缓存，解决redis频繁请求访问设备对象的问题
 * @author: LIULEI-TGL[知行合一]
 * @create: 2021-09-23 14:26:
 **/
@Service
@Slf4j
public class DeviceInfoCache implements DeviceInfoCacheManager{

    @Value("${springext.cache.redis.topic:cacheDeviceInfo}")
    String topicName;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 删除缓存设备对象
     * @param deviceId 设备id
     * @return
     */
    @CacheEvict(cacheNames = "cacheDevice", key = "#deviceId")
    public void delDeviceInfo(String deviceId){
        log.info("待删除的缓存设备,设备IDdeviceID:>{}", deviceId);
        Assert.notNull(deviceId,"设备ID不可用.");
    }

    /**
     * 新增缓存设备对象
     * @param deviceId
     * @param deviceEntity
     * @return
     */
    @CachePut(cacheNames = "cacheDevice", key = "#deviceId")
    public DeviceEntity putDeviceInfo(String deviceId, DeviceEntity deviceEntity){
        log.info("待更新的缓存设备,设备IDdeviceID:>{}", deviceId);
        Assert.notNull(deviceId,"设备ID不可用.");
        Assert.notNull(deviceEntity,"设备对象不可用.");
        return deviceEntity;
    }
//
//    @CachePut(cacheNames = "cacheDevice", key = "#hphm + #hpys" )
//    public DeviceEntity putDeviceInfotj(String hphm,String hpys, DeviceEntity deviceEntity){
//        log.info("待更新的缓存设备,设备IDdeviceID:>{}", deviceId);
//        Assert.notNull(deviceId,"设备ID不可用.");
//        Assert.notNull(deviceEntity,"设备对象不可用.");
//        return deviceEntity;
//    }

    /**
     * 检索指定设备的缓存数据对象
     * @param deviceId 设备ID
     * @return DeviceEntity
     */
    @Cacheable(cacheNames = "cacheDevice", key = "#deviceId", sync = true)
    public DeviceEntity getDeviceInfo(String deviceId){
        // 一级缓存里头查不到时，直接去二级缓存redis中进行检索
        return getDeviceInfo(deviceId, new DeviceEntity());
    }

    /**
     * 查询缓存设备
     * @param deviceId 设备id
     * @return
     */
    public DeviceEntity getDeviceInfo(String deviceId, DeviceEntity deviceEntity){
        System.out.println("待查询的设备ID为:>>" + deviceId);
        Assert.notNull(deviceId,"设备ID不可用.");

        // 查询redis
        // db
        return deviceEntity;
    }

    /**
     * 发布设备对象信息，到redis指定频道中。
     * 1、实现设备数据添加、更新的订阅/发布操作
     * @param deviceEntity
     */
    public void pubLishDeviceInfo(DeviceEntity deviceEntity){
        redisUtil.pusLish(topicName, JSON.toJSON(deviceEntity));
    }
}
