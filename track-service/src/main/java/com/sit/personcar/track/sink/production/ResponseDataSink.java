package com.sit.personcar.track.sink.production;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableRunnable;
import com.alibaba.fastjson.JSON;
import com.sit.personcar.track.analysis.vo.ResponseViewData;
import com.sit.personcar.track.models.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ResponseDataSink {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseDataSink.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 将获取到的轨迹数据异步写入消息中间件
     * @param responseViewData
     */
    public void trackDataSinkKafka(ResponseViewData responseViewData){
        if (Objects.isNull(responseViewData) || Objects.isNull(responseViewData.getApp_header())
                || responseViewData.getApp_header().getStatus().equalsIgnoreCase(Tools.STATUS)
                || Objects.isNull(responseViewData.getApp_body())
                || Objects.isNull(responseViewData.getApp_body().getResult())
                || Tools.isEmpty(responseViewData.getServerId())){
            return;
        }

        new Fiber<>(new SuspendableRunnable(){
            @Override
            public void run() throws SuspendExecution, InterruptedException {
                List<Map<String, String>> temp = responseViewData.getApp_body().getResult();
                temp.stream().parallel().forEach(data -> {
                    data.put(Tools.SERVICEID, responseViewData.getServerId());

                    LOG.debug("responseData is value data:>{}", data);
                    kafkaTemplate.send(responseViewData.getServerId(), JSON.toJSONString(data));
                });
            }
        }).start();
    }
}
