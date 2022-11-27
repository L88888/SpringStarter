package com.sailing.commons.data.mq.starter;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.remoting.RPCHook;

/**
 * @program: es-spring-boot-starter-pom
 * @description: 封装rocketmq数据生产对象
 * @author: LIULEI
 * @create: 2020-07-30 17:51:
 **/
public class SailingMqProducer extends DefaultMQProducer {
    public SailingMqProducer(){
        super();
    }

    public SailingMqProducer(RPCHook rpcHook) {
        super(rpcHook);
    }

    public SailingMqProducer(final String producerGroup) {
        super(producerGroup);
    }

    public SailingMqProducer(final String producerGroup, RPCHook rpcHook, boolean enableMsgTrace,
                             final String customizedTraceTopic) {
        super(producerGroup, rpcHook, enableMsgTrace, customizedTraceTopic);
    }

    public SailingMqProducer(final String namespace, final String producerGroup) {
        super(namespace, producerGroup);
    }

    public SailingMqProducer(final String producerGroup, RPCHook rpcHook) {
        super(producerGroup, rpcHook);
    }

    public SailingMqProducer(final String namespace, final String producerGroup, RPCHook rpcHook) {
        super(namespace, producerGroup, rpcHook);
    }

    public SailingMqProducer(final String producerGroup, boolean enableMsgTrace) {
        super(producerGroup, enableMsgTrace);
    }

    public SailingMqProducer(final String producerGroup, boolean enableMsgTrace, final String customizedTraceTopic) {
        super( producerGroup, enableMsgTrace, customizedTraceTopic);
    }

    public SailingMqProducer(final String namespace, final String producerGroup, RPCHook rpcHook,
                             boolean enableMsgTrace, final String customizedTraceTopic) {
        super(namespace, producerGroup, rpcHook, enableMsgTrace, customizedTraceTopic);
    }
}
