package com.tgl.designpattern.service.builder;

import java.util.List;

/**
 * @program: spring-starter
 * @description: 创建用户对象建造过程
 * @author: LIULEI-TGL[知行合一]
 * @create: 2021-10-09 15:35:
 **/
public interface IUserBuilder {

    /**
     * 建造用户昵称
     * @return 用户昵称
     */
    default public String buildNicaname(){
        return "卡布奇诺";
    }

    /**
     * 用户消费次数
     * @return
     */
    int buildPayCnt();

    /**
     * 用户消费金额
     * @return
     */
    int buildPayAnt();

    /**
     * 用户经常浏览的产品类型
     * @return
     */
    List<String> buildProductType();

    /**
     * 用户经常浏览的产品价格区间
     * @return
     */
    List<String> buildAntInterval();

    /**
     * 获取用户User对象
     * @return
     */
    User getUser();
}