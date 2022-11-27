package com.sailing.service.highconcurrency;

import com.sailing.linkstrack.bo.ThirdPartyLog;
import com.sailing.service.servicetrack.ServiceTrackProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: spring-starter
 * @description: 实现多线程API
 * @author: LIULEI
 * @create: 2020-08-18 20:33:
 **/
@Slf4j
@Service("identifcationServiceImpl")
public class IdentifcationServiceImpl implements IdentifcationService {

    @Autowired
    private ServiceTrackProcess serviceTrackProcess;

    /**
     * 将业务处理加入到线程池中
     * 短任务
     */
    @Async("asyncTaskExecutor")
    @Override
    public void queryFaceInfo(int i){
        try {
            // 每一个线程执行10次，每次执行停留2秒
            for (int y =0;y<i;y++){
//                Thread.sleep(2000);
            }

            log.info("线程名称:>{},线程Id:>{}", Thread.currentThread().getName(),Thread.currentThread().getId());
            ThirdPartyLog thirdPartyLog = new ThirdPartyLog();
            thirdPartyLog.setName("测试接口A00" + i);
            thirdPartyLog.setMethod("POST");
            thirdPartyLog.setUrl("http://127.0.0.1:3232/strac/gather/v1");
            thirdPartyLog.setParams("ab=1");
            thirdPartyLog.setVendor("熙菱");
            thirdPartyLog.setDescription("测试接口A00" + i + ";高并发场景下的sqlite锁表问题浮现");
            thirdPartyLog.setResult("ab=1");
            thirdPartyLog.setUserCode("user07");
            log.info("请求对象报文为:>{}",thirdPartyLog);

            log.info("======================");
            Map res = serviceTrackProcess.gatherV1(thirdPartyLog);
            log.info("+++++++++++++++++++++++");
            log.info("res:>{}",res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
