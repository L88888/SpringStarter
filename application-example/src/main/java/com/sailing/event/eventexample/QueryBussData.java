package com.sailing.event.eventexample;

import com.sailing.event.LogsEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: es-spring-boot-starter-pom
 * @description: 处理业务数据并发布日志事件
 * @author: LIULEI
 * @create: 2020-06-08 15:45:
 **/
@Component
@Slf4j
public class QueryBussData {

    /**
     * 用于同步Event模式
     */
//    @Autowired
//    private ApplicationContext applicationContext;

    /**
     * 用于异步Event模式
     */
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private LogsEvent logsEvent;

    // 初始化加载LogsEvent事件对象
    private void setLogsEvent(Map eventData){
        this.logsEvent = new LogsEvent(this, eventData);
    }

    /**
     * 模拟生产数据流对象，既是异步事件发布
     * @param params
     */
    public void onMessage(List params){
        if (params.isEmpty() || params.size() == 0){
            log.info("无法对空数据进行业务处理");
            return;
        }

        log.info("开始生产事件数据对象,发布日志数据监听事件.");
        Map bussData = new ConcurrentHashMap();
        bussData.put("key", params);
        this.setLogsEvent(bussData);
        // todo 同步模式
//        applicationContext.publishEvent(this.logsEvent);

        // TODO 异步事件
        applicationEventPublisher.publishEvent(this.logsEvent);
        log.info("日志数据监听事件发布,已完成.");
    }
}
