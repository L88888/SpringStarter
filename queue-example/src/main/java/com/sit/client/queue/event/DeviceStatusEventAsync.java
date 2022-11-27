package com.sit.client.queue.event;


import com.sit.client.device.entity.DeviceStatusDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeviceStatusEventAsync {

    private DeviceStatusDto deviceStatusDto;
}
