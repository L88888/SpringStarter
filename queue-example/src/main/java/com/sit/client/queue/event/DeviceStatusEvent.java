package com.sit.client.queue.event;

import com.sit.client.device.entity.DeviceStatusDto;
import org.springframework.context.ApplicationEvent;

public class DeviceStatusEvent extends ApplicationEvent {

    private DeviceStatusDto deviceStatusDto;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DeviceStatusEvent(Object source, DeviceStatusDto deviceStatusDto) {
        super(source);
        this.deviceStatusDto = deviceStatusDto;
    }

    public DeviceStatusDto getDeviceStatusDto() {
        return deviceStatusDto;
    }
}
