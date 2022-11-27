package com.sit.client.example.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * @program: spring-starter
 * @description: 设备对象
 * @author: LIULEI-TGL
 * @create: 2021-09-23 14:30:
 **/
@Getter
@Setter
public class DeviceEntity implements Serializable{

    public DeviceEntity(){}

    private String deviceId;

    private String deviceName;

    private double longitude;

    private double latitude;

    public DeviceEntity(Builder builder){
        this.deviceId = builder.deviceId;
        this.deviceName = builder.deviceName;
        this.longitude = builder.longitude;
        this.latitude = builder.latitude;
    }

    /**
     * 编译对象Builder
     * {"deviceId":"1234567890qwertyu","deviceName":"三会门口","longitude":107.12345,"latitude":23.123}
     */
    public static final class Builder {
        String deviceId;

        String deviceName;

        double longitude;

        double latitude;

        public Builder deviceId(String deviceId){
            this.deviceId = deviceId;
            return this;
        }

        public Builder deviceName(String deviceName){
            this.deviceName = deviceName;
            return this;
        }

        public Builder longitude(double longitude){
            this.longitude = longitude;
            return this;
        }

        public Builder latitude(double latitude){
            this.latitude = latitude;
            return this;
        }

        public DeviceEntity build(){
            return new DeviceEntity(this);
        }
    }
}
