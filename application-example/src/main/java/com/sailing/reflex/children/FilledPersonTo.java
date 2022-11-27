package com.sailing.reflex.children;

import com.sailing.reflex.parent.WorkingHours;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @program: es-spring-boot-starter-pom
 * @description:
 * @author: LIULEI
 * @create: 2020-07-31 09:05:
 **/
@Slf4j
@Component
public class FilledPersonTo extends WorkingHours{
    public FilledPersonTo(){}

    /**
     * 员工2填报工时
     * @param name
     * @param time
     */
    public void onTimeFilled(String name,int time){
        log.info("FilledPersonTo,员工2开始填报工时名称{},时间{}", name, time * 2);
    }
}
