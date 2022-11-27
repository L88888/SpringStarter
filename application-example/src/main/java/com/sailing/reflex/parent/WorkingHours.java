package com.sailing.reflex.parent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @program: es-spring-boot-starter-pom
 * @description: 每月工时填报父类
 * @author: LIULEI
 * @create: 2020-07-31 09:04:
 **/
@Slf4j
@Component
public class WorkingHours {

    public WorkingHours(){
        log.info("Parent,父子类模式构建");
    }

    /**
     * 填报人以及工时时间
     * @param name
     * @param time
     */
    public void onTimeFilled(String name,int time){
        log.info("Parent,填报人{},填报工时时间{}",name, time);
    }

    /**
     * 填报人以及工时时间、月份
     * @param name 填报人
     * @param month 月份
     * @param time 工时时间
     */
    public void onTimeFilled(String name,String month,int time){
        log.info("Parent,填报人{},填报月份{},填报工时时间{}",name, month, time);
    }
}
