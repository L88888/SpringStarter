package com.sailing.service.production;

import com.alibaba.fastjson.JSON;
import com.sailing.commons.data.mq.starter.RocketMqProperties;
import com.sailing.commons.data.mq.starter.SailingMqProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: es-spring-boot-starter-pom
 * @description: 业务消息生产至RocketMq
 * @author: LIULEI
 * @create: 2020-07-24 15:36:
 **/
@Slf4j
@Component
public class ProdectBussDataMQ {

    @Autowired
    private SailingMqProducer producer;

    @Resource
    private RocketMqProperties rocketMqProperties;

    /**
     * 生产业务数据至RocketMQ
     * @throws Exception
     */
    public void prodectBussData(String tagName, String key) {
        try {
            // 生产这块需要更换一个方式重构一下
            if (producer != null){
                String bussData = "人脸实时报警数据报文.";
                Map faceAlarmData = new HashMap();
                // 设备名称
                faceAlarmData.put("sbmc", "丈八路神州数码科技园");
                // 纬度
                faceAlarmData.put("wd", "108.86781704711913");
                // 经度
                faceAlarmData.put("jd", "34.21301801546094");
                // 人脸出现时间
                faceAlarmData.put("faceTime", "2020-08-04 16:28:23");

                // 创建RocketMq Message消息对象
                // 发送的业务数据需要进行序列化，合适的情况下需要进行数据加密
                Message message = new Message(rocketMqProperties.getTopic(),tagName,key, JSON.toJSONBytes(faceAlarmData));
                SendResult sendResult = producer.send(message);
                log.info("[发送业务消息结果为:>>>{}]", sendResult.getSendStatus());
            }else{
                log.info("[RocketMq 对象初始化失败,请检查rocketmq-start组件配置信息.]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
