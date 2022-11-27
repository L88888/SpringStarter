package com.tgl.raftclient.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @program: spring-starter
 * @description: 封装停止线程的工具类
 * @author: LIULEI-TGL
 * @create: 2021-05-20 15:27:
 **/
@Slf4j
public class TglRaftThreadHelper {

    /** 获取当前机器的有效cpu执行数量 */
    public static int cpu = Runtime.getRuntime().availableProcessors();

    /** 核心线程数量默认 cpu * 2 */
    public static int maxPoolSize = cpu * 20;

    /** 队列最大容量 */
    public static final int queueSize = 5000;

    /** 超时时间 */
    public static final int keepTime = 1000 * 60;

    /** 超时时间单位 */
    public static final TimeUnit keepTimeUnit = TimeUnit.MILLISECONDS;

    /**
     * 主线程停止一段时间
     * @param milliseconds
     */
    public static void sleep(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.warn("线程停止失败:>>{}" , e.fillInStackTrace().toString());
        }
    }
}