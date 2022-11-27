package com.sailing.dcl;

/**
 * @program: es-spring-boot-starter-pom
 * @description: 采用DCL方式，处理单例在多线程模式下被多次创建的问题
 * @author: LIULEI
 * @create: 2020-06-22 14:49:
 **/
public class EventLogs {
     private volatile static EventLogs EVENTLOGS;

     private EventLogs(){}

    /**
     * 采用DCL方式，处理单例在多线程模式下被多次创建的问题
     * cpu指令乱序、重排
     * @return
     */
     public static EventLogs getInstance(){
         if (EVENTLOGS == null){
             // 局部枷锁，提升性能 Double Check Lock(DCL)多次检查锁机制
             synchronized (EventLogs.class){
                 if (EVENTLOGS == null){
                     EVENTLOGS = new EventLogs();
                 }
             }
         }
         return EVENTLOGS;
     }
}
