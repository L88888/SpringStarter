package com.sit.client.ckhelper.entity;

import java.io.Serializable;
import java.util.UUID;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-14 19:23:
 **/
public class User implements Serializable{
//    public final UUID device_id;
//    public final String metric;
//    public final Long time;
//    public final Double value;

    public User(){}

    public User(UUID device_id, String metric, Long time, Double value) {
        this.deviceId = device_id;
        this.metric = metric;
        this.time = time;
        this.value = value;
    }

    private UUID deviceId;
    private String metric;
    private Long time;
    private Double value;

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "[device_id=" + deviceId + ", metric=" + metric + ", time=" + time + ", value=" + value + "]";
    }
}
