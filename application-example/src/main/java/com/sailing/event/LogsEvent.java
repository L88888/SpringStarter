package com.sailing.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: es-spring-boot-starter-pom
 * @description: 定义采用事件机制收集系统业务日志
 * @author: LIULEI
 * @create: 2020-06-08 15:12:
 **/
@Data
public class LogsEvent extends ApplicationEvent{

    // 自定义事件属性dataLogs
    private Map dataLogs;
    // 自定义事件属性objDataLogs
    private Object[] objDatalogs;

    // 事件驱动参数传递map集合
    public LogsEvent(Object source, Map dataLogs) {
        super(source);
        this.dataLogs = dataLogs;
    }

    // 事件驱动参数传递 Object[] 数组
    public LogsEvent(Object source, Object[] objDatalogs){
        super(source);
        this.objDatalogs = objDatalogs;
    }
}
