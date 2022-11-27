package com.sailing.reflex.children;

import com.sailing.reflex.parent.WorkingHours;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @program: es-spring-boot-starter-pom
 * @description: 每月工时填报人one
 * @author: LIULEI
 * @create: 2020-07-31 09:04:
 **/
@Slf4j
@Component
public class FilledPersonOne extends WorkingHours {

    public FilledPersonOne(){}

    /**
     * 员工1填报工时
     * @param name
     * @param time
     */
    public void onTimeFilled(String name,int time){
        log.info("childrenOne,员工1开始填报工时名称{},时间{}", name, time * 8);
    }

    /**
     * 填报人以及工时时间、月份
     * @param name 填报人
     * @param month 月份
     * @param time 工时时间
     */
    public void onTimeFilled(String name,String month,int time){
        log.info("childrenOne,填报人{},填报月份{},填报工时时间{}",name, month, time - 1000);
    }
}
