package com.test.examples.dispeldoubts;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @program: spring-starter
 * @description: Java中迷惑的地方,内存使用量计算
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-01-20 12:35:
 **/
@Slf4j
public class DispelDoubts {

    /**
     * 计算内存占用情况
     */
    @Test
    public void setMemoryData(){
        // jvm内存总大小
        long total = Runtime.getRuntime().totalMemory();
        // jvm内存已使用大小
        long free = Runtime.getRuntime().freeMemory();
        long beforeVal = total - free;
        log.info("beforeUsedMemory:>{}", beforeVal);
        // jvm内存最大值
        long max = Runtime.getRuntime().maxMemory();

        // 申请20万的设备内存
        int arrayNum = 20 * 10000;
        if (false){
            arrayStorage(arrayNum);
        }else {
            linkStorage(arrayNum);
        }

        // jvm内存总大小
        long totalAfter = Runtime.getRuntime().totalMemory();
        // jvm内存已使用大小
        long freeAfter = Runtime.getRuntime().freeMemory();
        long afterVal = totalAfter - freeAfter;
        log.info("afterUsedMemory:>{}", afterVal);
        // 程序目前已使用内存大小
        long use = (afterVal - beforeVal) / 1024 / 1024;

        log.info("total={},free={},use={}(MB)", total, free, use);
        try {
            Thread.sleep(1000 * 1000 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用链表的方式存储
     * @param len
     */
    private void linkStorage(int len){
        List deviceData = new ArrayList<Device>(len);

        // 设备编号
        String deviceId = "";
        // 设备组织
        String deviceOrg = "";
        // 设备名称
        String deviceName = "";
        // 设备状态
        String deviceZt = "";
        // 设备类型
        String deviceLx = "";
        // 设备图标
        String deviceTb = "";
        // 设备编号:31011020220120144312
        for (int i =0;i< len;i++){
            // 设备编号
            deviceId = "31011020220120144312" + i;
            // 设备组织
            deviceOrg = "赵巷派出所-1120" + i;
            // 设备名称
            deviceName = "赵巷南光路与菜市场十字向西100米" + i;
            // 设备状态
            deviceZt = "1";
            // 设备类型
            deviceLx = "视频";
            // 设备图标
            deviceTb = "video";

            deviceData.add(new Device(deviceId,deviceOrg,deviceName,
                    deviceZt,deviceLx,deviceTb));
        }
    }

    /**
     * 使用数据方式存储
     * @param len
     */
    private void arrayStorage(int len) {
        Device[] deviceData = new Device[len];

        // 设备编号
        String deviceId = "";
        // 设备组织
        String deviceOrg = "";
        // 设备名称
        String deviceName = "";
        // 设备状态
        String deviceZt = "";
        // 设备类型
        String deviceLx = "";
        // 设备图标
        String deviceTb = "";
        // 设备编号:31011020220120144312
        for (int i =0;i< len;i++){
            // 设备编号
            deviceId = "31011020220120144312" + i;
            // 设备组织
            deviceOrg = "赵巷派出所-1120" + i;
            // 设备名称
            deviceName = "赵巷南光路与菜市场十字向西100米" + i;
            // 设备状态
            deviceZt = "1";
            // 设备类型
            deviceLx = "视频";
            // 设备图标
            deviceTb = "video";

            deviceData[i] = new Device(deviceId,deviceOrg,deviceName,
                    deviceZt,deviceLx,deviceTb);
        }
    }

    class Device implements Serializable{
        // 设备编号
        String deviceId;
        // 设备组织
        String deviceOrg;
        // 设备名称
        String deviceName;
        // 设备状态
        String deviceZt;
        // 设备类型
        String deviceLx;
        // 设备图标
        String deviceTb;

        public Device(){}

        public Device(String deviceId, String deviceOrg, String deviceName,
                      String deviceZt, String deviceLx, String deviceTb) {
            this.deviceId = deviceId;
            this.deviceOrg = deviceOrg;
            this.deviceName = deviceName;
            this.deviceZt = deviceZt;
            this.deviceLx = deviceLx;
            this.deviceTb = deviceTb;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public void setDeviceOrg(String deviceOrg) {
            this.deviceOrg = deviceOrg;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public void setDeviceZt(String deviceZt) {
            this.deviceZt = deviceZt;
        }

        public void setDeviceLx(String deviceLx) {
            this.deviceLx = deviceLx;
        }

        public void setDeviceTb(String deviceTb) {
            this.deviceTb = deviceTb;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public String getDeviceOrg() {
            return deviceOrg;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public String getDeviceZt() {
            return deviceZt;
        }

        public String getDeviceLx() {
            return deviceLx;
        }

        public String getDeviceTb() {
            return deviceTb;
        }

        @Override
        public String toString() {
            return "Device{" +
                    "deviceId='" + deviceId + '\'' +
                    ", deviceOrg='" + deviceOrg + '\'' +
                    ", deviceName='" + deviceName + '\'' +
                    ", deviceZt='" + deviceZt + '\'' +
                    ", deviceLx='" + deviceLx + '\'' +
                    ", deviceTb='" + deviceTb + '\'' +
                    '}';
        }
    }

    private void processSafe(){
        String faceToken = "qweqw12312wqeqweQWEDS";

        ConcurrentMap c1 = new ConcurrentHashMap(100);

        if (Objects.isNull(c1) || Objects.isNull(c1.get(faceToken)) ||
                c1.containsKey(faceToken)){
            synchronized (c1){
                // 调用第三方api接口开始获取有效的人脸token数据
                c1.putIfAbsent(faceToken, faceToken);
            }
        }

        // 返回缓存中的人脸token认证数据
        c1.get(faceToken);
    }
}
