package com.tgl.binlogevent.app;

import com.tgl.binlogevent.binlogevent.CheckBinLogData;
import com.tgl.binlogevent.concurrent.EnableSpringBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-06-27 16:35:
 **/
// ComponentScan主要就是定义扫描的路径从中找出标识了需要装配的类自动装配到spring的bean容器中
@ComponentScan(value = {"com.tgl.binlogevent.*"})
@EnableScheduling
@EnableSpringBean
@SpringBootApplication
@Configuration
@EnableAsync
// 启用FeignClient,状态可选
@EnableFeignClients(value = {"com.tgl.binlogevent.*"})
public class RdbmsApplication {
    public static void main(String[] args) {
        CheckBinLogData.getInstance().eventListeners();
        SpringApplication.run(RdbmsApplication.class, args);
    }
}
