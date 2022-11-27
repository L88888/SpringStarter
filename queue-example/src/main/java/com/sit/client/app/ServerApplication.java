package com.sit.client.app;

import com.sailing.rpc.bootstart.NodeBootStarp;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @program: spring-starter
 * @description: 无锁队列(Disruptor)应用
 * @author: LIULEI-TGL
 * @create: 2021-06-27 16:35:
 **/
// ComponentScan主要就是定义扫描的路径从中找出标识了需要装配的类自动装配到spring的bean容器中
@ComponentScan(value = {"com.sit.client.*","com.sailing.rpc.*"})
@MapperScan(value = {"com.sit.client.*"})
@SpringBootApplication
@Configuration
@EnableAsync
@Slf4j
public class ServerApplication {

    public static void main(String[] args) {
        log.info("我是ServerApplication服务 boss.");
        int selfPort = 10288;
        NodeBootStarp.daemon(selfPort);
        SpringApplication.run(ServerApplication.class, args);
    }
}
