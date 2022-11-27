package com.sailing.commons.data.mq.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @program: es-spring-boot-starter-pom
 * @description: 消费者的Starter定义
 * @author: LIULEI
 * @create: 2020-07-31 19:44:
 **/
//@Slf4j
//@Configuration
//@EnableConfigurationProperties(RocketMqProperties.class)
public class RocketMqPushConsumerStarter implements DisposableBean{



    @Override
    public void destroy() throws Exception {

    }
}
