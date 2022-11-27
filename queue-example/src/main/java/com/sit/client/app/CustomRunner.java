package com.sit.client.app;

import com.sit.client.queue.handlerqueue.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 自定义服务程序启动时自动加载线程任务
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomRunner implements ApplicationRunner {

    private final TaskService taskService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("我是自定义的CustomRunner线程服务.");
        taskService.consumerDeviceStatusData();
    }
}
