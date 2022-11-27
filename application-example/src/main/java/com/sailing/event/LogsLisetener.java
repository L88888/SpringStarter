package com.sailing.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @program: es-spring-boot-starter-pom
 * @description: 事件监听处理器,异步事件
 * @author: LIULEI
 * @create: 2020-06-08 15:35:
 **/
@Component
@Slf4j
public class LogsLisetener implements ApplicationListener<LogsEvent>{

    /**
     * 绑定需要监听的事件对象,异步事件
     * 处理业数据对象
     */
    @Async
    @Override
    public void onApplicationEvent(LogsEvent logsEvent) {
        try {
            System.out.println("ssssssssssssssss");
            log.info("事件接收到的业务数据Start,is value:>>{}", logsEvent.getDataLogs());
            // 停顿三秒观察是否是异步event信息
            Thread.sleep(3000);
            log.info("事件接收到的业务数据End,is value:>>{}", logsEvent.getDataLogs());
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("处理事件数据对象异常,异常信息为:>{}", e.fillInStackTrace());
        }
    }
}
