package com.sailing.commons.data.mq.starter;

import org.apache.rocketmq.client.consumer.AllocateMessageQueueStrategy;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.remoting.RPCHook;

/**
 * @program: es-spring-boot-starter-pom
 * @description: 客户端简单封装消费者push对象,主动推送模式
 * @author: LIULEI
 * @create: 2020-07-31 19:46:
 **/
public class SailingMqPushConsumer extends DefaultMQPushConsumer {
    
    public SailingMqPushConsumer(){
        super();
    }
    
    public SailingMqPushConsumer(final String consumerGroup) {
        super(consumerGroup);
    }

    /**
     * Constructor specifying namespace and consumer group.
     *
     * @param namespace Namespace for this MQ Producer instance.
     * @param consumerGroup Consumer group.
     */
    public SailingMqPushConsumer(final String namespace, final String consumerGroup) {
        super(namespace, consumerGroup);
    }


    /**
     * Constructor specifying RPC hook.
     *
     * @param rpcHook RPC hook to execute before each remoting command.
     */
    public SailingMqPushConsumer(RPCHook rpcHook) {
        super(rpcHook);
    }

    /**
     * Constructor specifying namespace, consumer group and RPC hook .
     *
     * @param namespace Namespace for this MQ Producer instance.
     * @param consumerGroup Consumer group.
     * @param rpcHook RPC hook to execute before each remoting command.
     */
    public SailingMqPushConsumer(final String namespace, final String consumerGroup, RPCHook rpcHook) {
        super(namespace, consumerGroup, rpcHook);
    }

    /**
     * Constructor specifying consumer group, RPC hook and message queue allocating algorithm.
     *
     * @param consumerGroup Consume queue.
     * @param rpcHook RPC hook to execute before each remoting command.
     * @param allocateMessageQueueStrategy Message queue allocating algorithm.
     */
    public SailingMqPushConsumer(final String consumerGroup, RPCHook rpcHook,
                                 AllocateMessageQueueStrategy allocateMessageQueueStrategy) {
        super(consumerGroup, rpcHook, allocateMessageQueueStrategy);
    }

    /**
     * Constructor specifying namespace, consumer group, RPC hook and message queue allocating algorithm.
     *
     * @param namespace Namespace for this MQ Producer instance.
     * @param consumerGroup Consume queue.
     * @param rpcHook RPC hook to execute before each remoting command.
     * @param allocateMessageQueueStrategy Message queue allocating algorithm.
     */
    public SailingMqPushConsumer(final String namespace, final String consumerGroup, RPCHook rpcHook,
                                 AllocateMessageQueueStrategy allocateMessageQueueStrategy) {
        super(namespace, consumerGroup, rpcHook, allocateMessageQueueStrategy);
    }

    /**
     * Constructor specifying consumer group and enabled msg trace flag.
     *
     * @param consumerGroup Consumer group.
     * @param enableMsgTrace Switch flag instance for message trace.
     */
    public SailingMqPushConsumer(final String consumerGroup, boolean enableMsgTrace) {
        super(consumerGroup, enableMsgTrace);
    }

    /**
     * Constructor specifying consumer group, enabled msg trace flag and customized trace topic name.
     *
     * @param consumerGroup Consumer group.
     * @param enableMsgTrace Switch flag instance for message trace.
     * @param customizedTraceTopic The name value of message trace topic.If you don't config,you can use the default trace topic name.
     */
    public SailingMqPushConsumer(final String consumerGroup, boolean enableMsgTrace, final String customizedTraceTopic) {
        super(consumerGroup, enableMsgTrace, customizedTraceTopic);
    }


    /**
     * Constructor specifying consumer group, RPC hook, message queue allocating algorithm, enabled msg trace flag and customized trace topic name.
     *
     * @param consumerGroup Consume queue.
     * @param rpcHook RPC hook to execute before each remoting command.
     * @param allocateMessageQueueStrategy message queue allocating algorithm.
     * @param enableMsgTrace Switch flag instance for message trace.
     * @param customizedTraceTopic The name value of message trace topic.If you don't config,you can use the default trace topic name.
     */
    public SailingMqPushConsumer(final String consumerGroup, RPCHook rpcHook,
                                 AllocateMessageQueueStrategy allocateMessageQueueStrategy, boolean enableMsgTrace, final String customizedTraceTopic) {
        super(consumerGroup, rpcHook, allocateMessageQueueStrategy, enableMsgTrace, customizedTraceTopic);
    }

    /**
     * Constructor specifying namespace, consumer group, RPC hook, message queue allocating algorithm, enabled msg trace flag and customized trace topic name.
     *
     * @param namespace Namespace for this MQ Producer instance.
     * @param consumerGroup Consume queue.
     * @param rpcHook RPC hook to execute before each remoting command.
     * @param allocateMessageQueueStrategy message queue allocating algorithm.
     * @param enableMsgTrace Switch flag instance for message trace.
     * @param customizedTraceTopic The name value of message trace topic.If you don't config,you can use the default trace topic name.
     */
    public SailingMqPushConsumer(final String namespace, final String consumerGroup, RPCHook rpcHook,
                                 AllocateMessageQueueStrategy allocateMessageQueueStrategy, boolean enableMsgTrace, final String customizedTraceTopic) {
        super(namespace, consumerGroup, rpcHook, allocateMessageQueueStrategy, enableMsgTrace, customizedTraceTopic);
    }
}
