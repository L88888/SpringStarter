package com.tgl.example.device.ws;

import com.tgl.example.device.entity.DeviceEntity;
import com.tgl.example.device.server.DeviceBatchInfo;
import com.tgl.example.device.server.DeviceInfoCache;
import com.tgl.example.device.server.DeviceInfoCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-06-27 18:29:
 **/
@RestController
@Slf4j
public class TestWs {

    @Autowired
    private DeviceInfoCacheManager deviceInfoCache;

    @Autowired
    private DeviceInfoCacheManager deviceBatchInfo;

    /**
     * 测试批量数据提交入库clickhouse
     * curl http://127.0.0.1:2003/batch/data/v1
     * @return
     */
    @RequestMapping(value = "batch/data/v1",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map check() {
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");
//        vehicleService.insertVehicleinfo();
        return map;
    }

    /**
     * 注销登录的用户对象
     * curl -X GET "http://127.0.0.1:2003/external-interface/ajaxLogout" -H "accept: application/json" -H "Authorization: 123"
     * @param authorization
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "external-interface/ajaxLogout",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map ajaxLogout(@RequestHeader(name = "Authorization", required = true) String authorization)throws Exception {
        log.info("注销用户的token认证信息为:>{}", authorization);
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");
//        vehicleService.insertVehicleinfo();
        return map;
    }

    /**
     * 获取批量下载图片
     * http://127.0.0.1:2003/v1/downloadImages/data
     * @return
     */
    @RequestMapping(value = "v1/downloadImages/data",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map downloadImages() {
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");

//        downloadImagesImpl.getImages();
        return map;
    }

    /**
     * http://127.0.0.1:2003/v1/deviceInfoCache/data?deviceid=123478
     * @return
     */
    @RequestMapping(value = "v1/deviceInfoCache/data",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map deviceInfoCache(@RequestParam(name = "deviceid") String deviceid) {
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");

        // 开启循环
        map.put("data", deviceInfoCache.getDeviceInfo(deviceid));
        return map;
    }

    /**
     * http://127.0.0.1:2003/v1/deviceInfoPublish/data
     *
     * 待发布的数据结构
     * {"deviceId":"123478","deviceName":"三会门口","longitude":107.12345,"latitude":23.123}
     * @return
     */
    @RequestMapping(value = "v1/deviceInfoPublish/data",method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map deviceInfoPublish(@RequestBody DeviceEntity device) {
        Assert.notNull(device, "待发布的设备对象未定义.");
        deviceInfoCache.pubLishDeviceInfo(device);

        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");
        return map;
    }

    /**
     * http://127.0.0.1:2003/v1/devicebatchinfocache/data?deviceid=123478
     * @return
     */
    @RequestMapping(value = "v1/devicebatchinfocache/data",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map deviceBatchInfoCache(@RequestParam(name = "deviceid") String deviceid) {
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");

        String[] deviceIds = {deviceid};
        // 开启循环
        map.put("data", deviceBatchInfo.getBatchDeviceInfo(deviceIds));
        return map;
    }
}
