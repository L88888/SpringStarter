package com.sit.client.device.service;

import com.sit.client.device.entity.DeviceStatusDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据同步数据映射层
 * @author Albert
 */
@Mapper
@Component
public interface DbSyncMapper {

    /**
     * 查询设备状态
     * @return
     */
    List<DeviceStatusDto> queryDeviceStatus();

    /**
     * 清空状态临时表数据
     * @return
     */
    Integer clearDeviceStatusTemp();

    /**
     * 写入数据到状态临时表
     * @param statusList 状态信息集合
     * @return
     */
//    Integer insertDeviceStatusTemp(@Param(value = "statusList") List<DeviceStatusDto> statusList);

    Integer insertDeviceStatusTemp(DeviceStatusDto deviceStatusDto);

    /**
     * 同步设备状态临时表至设备信息表
     * @return
     */
    Integer syncDeviceStatus();

    /**
     * 同步设备状态临时表至设备信息表
     * @return
     */
    Integer syncDeviceStatusZtxx();

    /**
     * 同步设备状态至关系表
     * @return
     */
    Integer syncDeviceStatusRelation();
}
