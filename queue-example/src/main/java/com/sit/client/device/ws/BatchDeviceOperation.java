package com.sit.client.device.ws;

import com.sit.client.device.service.DeviceSynchronization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BatchDeviceOperation {

    private final DeviceSynchronization deviceSynchronization;

    /**
     * curl http://127.0.0.1:12002/v1/batchDeviceState
     * @return
     */
    @RequestMapping(value = "v1/batchDeviceState",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map batchDeviceState() {
        deviceSynchronization.startDeviceStatus();

        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "batch data save complete.");
        return map;
    }


}
