package com.sailing.commons.data.mq.starter;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @program: spring-starter
 * @description: RocketMqConfiguration 初始化配置rocketmq客户端，封装生产、消费api接口
 * @author: LIULEI
 * @create: 2020-06-27 17:35:RocketMqConfiguration
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties(RocketMqProperties.class)
public class RocketMqProductStarter implements DisposableBean {

    @Resource
    private RocketMqProperties rocketMqProperties;

    /**
     * default mq 生产者对象定义
     * @author: LIULEI
     */
    private SailingMqProducer producer;

    /**
     * 采用spring上下文获取消费端所有的实例对象
     * @author: LIULEI
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 从主体topic的最顶端（最开始）位置消费
     */
    private static final String OLD = "old";

    /**
     * 从主体topic的最新（最末尾）位置消费
     */
    private static final String NEW = "new";

    /**
     * 默认引用bean
     * @author: LIULEI
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(DefaultMQProducer.class)
    public SailingMqProducer initRocketMq(){
        log.info("初始化rocketmq的配置信息为:>>>{},{},{}", this.rocketMqProperties.getTopic(),
                this.rocketMqProperties.getGroupName(), this.rocketMqProperties.getNameServer());
        producer = new SailingMqProducer(this.rocketMqProperties.getGroupName());
        //不开启vip通道 开通后端口会减2
        producer.setVipChannelEnabled(false);
        //绑定name server
        producer.setNamesrvAddr(this.rocketMqProperties.getNameServer());
        // 发送消息的超时时间
        producer.setSendMsgTimeout(this.rocketMqProperties.getMsgTimeOut());
        start();
        return producer;
    }

    /**
     * 对象在使用之前必须要调用一次，只能初始化一次
     * @author: LIULEI
     */
    public void start(){
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * mq对象销毁方法
     * @author: LIULEI
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        if (this.producer != null){
            log.info("RocketMQ already closed.");
            this.producer.shutdown();
        }
    }

    /**
     * Springboot 启动时加载所有的消费者实例
     * @author: LIULEI
     */
    @PostConstruct
    private void initConsumer(){
        if (applicationContext != null){
            // 通过反射机制初始化消费者实例
            Map<String, SailingAbstractRocketConsumer> rocketConsumerMap =
                    applicationContext.getBeansOfType(SailingAbstractRocketConsumer.class);
            if (rocketConsumerMap == null || rocketConsumerMap.size() == 0 || rocketConsumerMap.isEmpty()){
                log.info("实例化对象SailingAbstractRocketConsumer失败,失败信息为{}", rocketConsumerMap);
            }else{
                rocketConsumerMap.forEach((k,v)->{
                    log.info("key{},value{}", k,v);
                    // 处理子类所有的业务实现
                    processClildrenConsumer(v);
                });
            }
        }else{
            log.info("Spring 上下文对象ApplicationContext初始化失败.");
        }
    }

    /**
     * 处理子类的消费模式
     * @author: LIULEI
     * @param rc
     */
    private void processClildrenConsumer(SailingAbstractRocketConsumer rc){
        try {
            // 初始化消费端
            rc.init();

            SailingMqPushConsumer sailingMqPushConsumer =
                    new SailingMqPushConsumer(this.rocketMqProperties.getGroupName());
            sailingMqPushConsumer.setNamesrvAddr(this.rocketMqProperties.getNameServer());
            sailingMqPushConsumer.setConsumeThreadMin(this.rocketMqProperties.getConsumeThreadMin());
            sailingMqPushConsumer.setConsumeThreadMax(this.rocketMqProperties.getConsumeThreadMax());
            sailingMqPushConsumer.registerMessageListener(rc.messageListenerConcurrently);

            // 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费 如果非第一次启动，那么按照上次消费的位置继续消费
            if (this.rocketMqProperties.getConsumePattern().equals(OLD)){
                sailingMqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            }else{
                sailingMqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            }
            // 每次消息提交数量
            sailingMqPushConsumer.setConsumeMessageBatchMaxSize(this.rocketMqProperties.getConsumeMessageBatchMaxSize());
            // 主题
            // 消息标签,指定标签消费业务数据 "tag1 || tag2 || tag3",全量消息消费tags为*
            sailingMqPushConsumer.subscribe(rc.topics, rc.tags);
            // 启动实时监听
            sailingMqPushConsumer.start();
            rc.sailingMqPushConsumer = sailingMqPushConsumer;
        } catch (MQClientException e) {
            log.debug("初始化客户端消费实例异常，异常信息为{}", e.fillInStackTrace());
        }
    }
}