package com.sailing.commons.data.mq.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: es-spring-boot-starter-pom
 * @description: RocketMqProperties rocketmq 基础配置信息
 * @author: LIULEI
 * @create: 2020-06-27 17:37:
 **/
@Data
@ConfigurationProperties(prefix="com.sit.rocketmq")
public class RocketMqProperties {

    /**
     * Name Server 地址，因为是集群部署 所以有多个用 分号 隔开
     * 192.168.80.20:9876;192.168.80.21:9877
     */
    private String nameServer = "182.168.80.20:10911";

    /**
     * 主题名称 主题一般是服务器设置好 而不能在代码里去新建topic（ 如果没有创建好，生产者往该主题发送消息 会报找不到topic错误）
     */
    private String topic = "LL-Test-2020-0627";

    /**
     * 生产者与消费者的组名称定义
     */
    private String groupName = "test_producer";

    /**
     * 消息生产超时时间,单位秒
     */
    private int msgTimeOut = 5000;

    /**
     * 客户端消费的最小线程数
     */
    private int consumeThreadMin = 5;

    /**
     * 客户端消费的最大线程数
     */
    private int consumeThreadMax = 10;

    /**
     * 每次消息提交数量
     */
    private int consumeMessageBatchMaxSize = 1;

    /**
     * 消费模式
     * old/new
     */
    private String consumePattern = "old";
}
