package com.sailing.service.highconcurrency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @program: spring-starter
 * @description: 身份信息落地,开线程并发处理。需要使用线程池对线程进行管理
 * @author: LIULEI
 * @create: 2020-08-18 16:55:
 **/
@Slf4j
@Component
public class Identification {

    /**
     * 接口注入
     */
    @Autowired
    private IdentifcationService identifcationServiceImpl;

    /**
     * 初始化数据采集
     */
    public void initDataGather(int pi){
        RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
        for (int i =0;i< pi;i++){
            identifcationServiceImpl.queryFaceInfo(pi);
        }
    }
}
