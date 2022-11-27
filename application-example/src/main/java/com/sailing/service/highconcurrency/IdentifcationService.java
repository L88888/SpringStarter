package com.sailing.service.highconcurrency;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI
 * @create: 2020-08-18 20:32:
 **/
public interface IdentifcationService {

    /**
     * 定义一个多线程的接口API
     * @param i
     */
    public void queryFaceInfo(int i);
}
