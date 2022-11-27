//package com.sailing.tgl.tests.device;
//
//import com.alibaba.fastjson.JSON;
//import Application;
//import DeviceEntity;
//import com.tgl.example.example.server.DeviceInfoCache;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.lang.ref.WeakReference;
//import java.models.ArrayList;
//import java.models.List;
//
///**
// * @program: spring-starter
// * @description:
// * @author: LIULEI-TGL
// * @create: 2021-09-23 15:24:
// **/
//@Slf4j
//@RunWith(SpringRunner.class)
////@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(classes = Application.class)
//public class TestDeviceCache {
//
//    @Autowired
//    private DeviceInfoCache deviceInfoCache;
//
//    @Test
//    public void testDeviceInfoData(){
//        deviceInfoCache.getDeviceInfo("23456789dddd");
//
//        String tt = "{\"deviceId\":\"23456789wertyui\",\"deviceName\":\"三会门口\",\"longitude\":107.12345,\"latitude\":23.123}";
//        JSON.parse(tt.getBytes());
//        DeviceEntity dd = new DeviceEntity();
//        WeakReference<DeviceEntity> devices = new WeakReference<DeviceEntity>(dd);
//    }
//}