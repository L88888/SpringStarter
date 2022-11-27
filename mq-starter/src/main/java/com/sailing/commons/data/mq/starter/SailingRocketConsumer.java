package com.sailing.commons.data.mq.starter;

import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.springframework.stereotype.Component;

/**
 * @program: es-spring-boot-starter-pom
 * @description: 自定义消费者核心约束
 * @author: LIULEI
 * @create: 2020-08-01 22:56:
 **/
@Component
public interface SailingRocketConsumer {

    /**
     * 消费者初始化RocketMq
     */
    public abstract void init();

    /**
     * 自定义监听事件(push、pull)
     * @param messageListener
     */
    public void registerMessageListener(MessageListenerConcurrently messageListener);
}
