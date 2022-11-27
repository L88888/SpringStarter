package com.tgl.rdbms.entity;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-07-11 16:59:
 **/
public class CommodityOrderQueue {

    public static LinkedBlockingQueue<CheckTransInfoDto> TEMPDATATO = new LinkedBlockingQueue<CheckTransInfoDto>();
}
