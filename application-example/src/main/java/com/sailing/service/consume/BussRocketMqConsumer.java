package com.sailing.service.consume;

import com.sailing.commons.data.mq.starter.RocketMqProperties;
import com.sailing.commons.data.mq.starter.SailingAbstractRocketConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: es-spring-boot-starter-pom
 * @description: RocketMq消费端应用
 * @author: LIULEI
 * @create: 2020-08-01 23:41:
 **/
@Slf4j
@Component
public class BussRocketMqConsumer extends SailingAbstractRocketConsumer {

    @Resource
    private RocketMqProperties rocketMqProperties;

    /**
     * 消费端开始接收消息数据
     */
    @Override
    public void init() {
        // 客户端消费自己的主题数据
        String tag = "*";
        tag = "face-002";
        super.necessary(rocketMqProperties.getTopic(),"",tag,"");
        log.info("客户端开始消费业务数据,消费主题:>{},消费主题的标签:>{},自定义组名称:>{},自定义消费标题:>{}",
                super.topics, super.tags, super.groupName,super.consumerTitel);
        registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                // 处理业务数据
                msgs.stream().forEach(msg ->{
                    log.info("接收到服务端发来的消息数据为:>{},主题:>{},标签:>{},消息id:>{}", new String(msg.getBody()), msg.getTopic(),
                            msg.getTags(), msg.getMsgId());
                });
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
    }
}
