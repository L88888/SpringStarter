package com.sailing.cglib.test;

import java.util.Date;

/**
 * @program: spring-starter
 * @description: 通过cglib来监听人脸对象中所有方法的动态执行，尝试获取方法的入参以及返回值对象
 * @author: LIULEI
 * @create: 2021-04-27 20:10:
 **/
public class Face {

    /**
     * 返回人脸抓拍的时间
     * @return
     */
    final public String facePassTime(String name){
        System.out.println("当事人==>" + name);
        return new Date().toString();
    }

    public String facePassTime(String name,String name2){
        System.out.println("当事人==>" + name + name2);
        return new Date().toString();
    }

    /**
     * 人脸1:1比对
     * @param nameOne
     * @param nameTo
     */
    private void faceCompare(String nameOne,String nameTo){
        System.out.println("当事人==>" + nameOne + nameTo);
    }

    /**
     * 查询人脸汇聚数据对象
     * @param facePassTime
     */
    public void faceQuery(String facePassTime){
        System.out.println("当事人,经过时间为==>" + facePassTime);
    }

}
