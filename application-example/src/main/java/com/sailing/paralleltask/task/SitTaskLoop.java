package com.sailing.paralleltask.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SitTaskLoop
 * @Description 任务执行的入口
 * @Author Liulei
 * @Date 2022/11/24 22:00
 * @Version 1.0
 **/
@Slf4j
public class SitTaskLoop {

    public SitTaskLoop(){
        this.initTask();
    }

    // 创建5个任务
    // 多线程执行每个任务
    // 持续时间10秒钟，10秒后任务开始停止执行

    List<ChildTask> tasks = new ArrayList<>(100);

    /**
     * 初始化5个任务(ChildTask类)
     */
    private void initTask(){
        tasks.add(new ChildTask("childTask1"));
        tasks.add(new ChildTask("childTask2"));
        tasks.add(new ChildTask("childTask3"));
        tasks.add(new ChildTask("childTask4"));
        tasks.add(new ChildTask("childTask5"));
    }

    /**
     * 启动初始化的任务对象集合
     */
    public void startTask(){
        if (CollectionUtils.isEmpty(tasks)){
            log.info("无法启动未定义的任务对象:>{}", tasks);
            return;
        }

        for (final ChildTask task : tasks){
//            SitTaskProcess.getOrInitExecutor("", ); todo 后期扩展
            new Thread(()->{
                task.doExecutor();
            }).start();
        }
    }

    /**
     * 停止任务对象集合
     */
    public void stopTask(){
        if (CollectionUtils.isEmpty(tasks)){
            log.info("无法停止未定义的任务对象:>{}", tasks);
            return;
        }

        for (final ChildTask task : tasks){
            task.jvmTerminal();
        }
    }
}