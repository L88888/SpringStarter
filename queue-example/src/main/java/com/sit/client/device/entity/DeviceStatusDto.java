package com.sit.client.device.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * 设备状态
 */
@Data
@Builder
public class DeviceStatusDto implements Serializable {

    private static final long serialVersionUID = -6458299224638755792L;

    /** 设备ID */
    private String deviceId;

    /** 设备状态 */
    private String status;

    /** 时间戳 */
    private long writeTimeMillis;

    /** 时间 */
    private String writeTime;

    private String deviceName;

    private String deviceArea;

    private String longitude;

    private String latitude;

    private String cameraType;

    private String cameraName;

    @Tolerate
    public DeviceStatusDto() {
    }
}
