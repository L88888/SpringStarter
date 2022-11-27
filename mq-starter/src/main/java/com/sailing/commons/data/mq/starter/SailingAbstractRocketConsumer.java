package com.sailing.commons.data.mq.starter;

import lombok.Data;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.springframework.stereotype.Component;

/**
 * @program: es-spring-boot-starter-pom
 * @description: 自定义消费父类对象,继承该父类的子类自动加载消费链
 * @author: LIULEI
 * @create: 2020-08-01 22:54:
 **/
@Data
@Component
public abstract class SailingAbstractRocketConsumer implements  SailingRocketConsumer {
    // 生产这与消费者消息主题topics
    protected String topics;
    // 消费者组名称
    protected String groupName;
    // 生产这与消费者消息标签tags
    protected String tags;
    // 消费者实时监听事件,父类监听子类实现
    protected MessageListenerConcurrently messageListenerConcurrently;
    // 自定义消费标记
    protected String consumerTitel;
    // 生产者主动推送消息模式
    protected SailingMqPushConsumer sailingMqPushConsumer;

    /**
     * 父类不需要直接实现，按需实现(有子类自己初始化)
     */
    public abstract void init();

    /**
     * 消费者必须初始化消费主题、以及对应的标签信息
     * @param topics 主题
     * @param groupName 消费者组id
     * @param tags 标签
     * @param consumerTitel 自定义标题
     */
    protected void necessary(String topics, String groupName, String tags, String consumerTitel){
        this.topics = topics;
        this.groupName = groupName;
        this.tags= tags;
        this.consumerTitel = consumerTitel;
    }

    /**
     * 父类实时监听消息事件，子类实现后将消息事件进行透传
     * @param messageListener
     */
    @Override
    public void registerMessageListener(MessageListenerConcurrently messageListener) {
        this.messageListenerConcurrently = messageListener;
    }
}
