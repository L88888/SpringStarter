package com.sailing.linkstrack;

import com.sailing.linkstrack.bo.ThirdPartyLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @program: spring-starter
 * @description: 通过队列的方式进行接口数据远程存储,避免拥挤现象出现
 * @author: LIULEI
 * @create: 2021-04-14 10:33:
 **/
@Service(value = "sinkApiInfo")
@Slf4j
public class SinkApiInfo {

    @Autowired
    private ServiceTrackData serviceTrackData;

    /**
     * 声明队列用于存放API接口追踪的对象信息
     */
    private ConcurrentLinkedQueue<ThirdPartyLog> apiInfoQueue = null;

    /**
     * 控制队列消费情况
     */
    private static boolean isRunning = false;

    /**
     * API版本信息
     */
    private static final String VERSION = "HUAWEI IVS3800.";

    public SinkApiInfo(){
        init();
    }

    private void init(){
        if (this.apiInfoQueue == null){
            // 预定义队列初始化大小为无界
            this.apiInfoQueue = new ConcurrentLinkedQueue<>();
        }
    }

    /**
     * 第三方API追入队列，生产队列
     * @param thirdPartyLog
     */
    public void pushInfo(ThirdPartyLog thirdPartyLog){
        try {
            this.init();
            this.apiInfoQueue.offer(thirdPartyLog);
            this.apiInfoQueue.add(thirdPartyLog);
            // 当队列中的追踪对象完全消费完了，在进行下一次的消费
            if (!this.isRunning){
                bussRun();
            }
        } catch (Exception e) {
            log.debug("第三方API追入队列失败,失败信息==>{}", e.fillInStackTrace());
        }
    }

    /**
     * 新增第三方API日志对象
     */
    @Async(value = "asyncTaskExecutor")
    public void addApiInfoData(Object resData, String methodName, Object[] args, Date exeTime, long costs){
        ThirdPartyLog thirdPartyLog = new ThirdPartyLog();
        thirdPartyLog.setId(UUID.randomUUID().toString());
        thirdPartyLog.setName(methodName);
        thirdPartyLog.setParamsAop(args);
        thirdPartyLog.setResult(
                resData == null ? "" : resData.toString());
        thirdPartyLog.setExeTime(exeTime);
        thirdPartyLog.setCosts(costs + "(ms)");
        thirdPartyLog.setEndTime(new Date().toString());
        this.pushInfo(thirdPartyLog);
    }

    /**
     * 异步消费队列中的追踪对象，将对象数据通过远程接口存储到服务追踪中
     */
    @Async(value = "asyncTaskExecutor")
    public void bussRun(){
        try {
            // 锁住消费队列
            this.isRunning = true;
            ThirdPartyLog thirdPartyLog = null;
            String exeTime = "";
            // 开始消费队里中的数据
            while (this.apiInfoQueue.size() > 0){
                thirdPartyLog = this.apiInfoQueue.poll();
                exeTime = thirdPartyLog.getExeTime().toString();
                thirdPartyLog.setVendor(VERSION);
                thirdPartyLog.setUrl(thirdPartyLog.getName());
                thirdPartyLog.setDescription(VERSION);
                thirdPartyLog.setMethod("未知");
                thirdPartyLog.setUserCode("admin-008");
                thirdPartyLog.setOptTime(exeTime);
                thirdPartyLog.setBeginTime(exeTime);
                thirdPartyLog.setParams(Arrays.toString(thirdPartyLog.getParamsAop()));
                thirdPartyLog.setResult(thirdPartyLog.getResultAop() == null ? "" : thirdPartyLog.getResultAop().toString());
                log.info("API追踪队列数据==>{}", thirdPartyLog);
                serviceTrackData.gatherV1(thirdPartyLog);
                Thread.sleep(10);
            }
        } catch (Exception e) {
            log.info("API追踪队列处理异常，异常信息==>{}",e);
        }finally {
            // 解锁消费队列
            this.isRunning = false;
        }
    }
}
