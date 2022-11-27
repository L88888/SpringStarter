package com.sailing.reflex;

import com.sailing.reflex.parent.WorkingHours;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @program: es-spring-boot-starter-pom
 * @description:采用spring getBeansOfType() 加载父类所有子类实例对象，并循环验证子类实现父类的方法
 * @author: LIULEI
 * @create: 2020-07-31 09:14:
 **/
@Slf4j
@Component
public class WorkInfo {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 启动时自动加载父类实例化的子类对象
     */
//    @PostConstruct
    private void initConsumer(){
        if (applicationContext != null){
            log.info(applicationContext.toString());
            // 通过反射机制初始化消费者实例
            Map<String, WorkingHours> workingHoursBean = applicationContext.getBeansOfType(WorkingHours.class);
            log.info("==={}", workingHoursBean.toString());
            // 执行工时填报函数
            if (workingHoursBean == null || workingHoursBean.size() == 0 || workingHoursBean.isEmpty()){
                log.info("实例化对象WorkingHours失败,失败信息为{}", workingHoursBean);
            }else{
                workingHoursBean.forEach((k,v)->{
                    log.info("key{},value{}", k,v);
                    // 处理子类所有的业务实现
                    processClildrenData(v);
                });
            }
        }else{
            log.info("spring上下文对象未加载{}", applicationContext);
        }
    }

    /**
     * 处理子类的业务数据对象
     * @param workingHours
     */
    private void processClildrenData(WorkingHours workingHours){
        String name = "张三";
        String month = "7月份工时";
        int time = 184;
        workingHours.onTimeFilled(name, time);

        workingHours.onTimeFilled(name,month, time);
    }
}
