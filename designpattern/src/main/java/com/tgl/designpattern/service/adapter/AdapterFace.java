package com.tgl.designpattern.service.adapter;

/**
 * @program: spring-starter
 * @description:  适配器只是做了一个外壳，内部真实的业务调用还是走的原有的方法实现
 * @author: LIULEI-TGL
 * @create: 2021-07-27 11:44:
 **/
public class AdapterFace implements Target {

    private AdapteeFace adapteeFace;

    /**
     * 采用类构造器的方式进行人脸预警适配
     * @param adapteeFace
     */
    public AdapterFace(AdapteeFace adapteeFace){
        this.adapteeFace = adapteeFace;
    }

    @Override
    public void request() {
        System.out.println("也可以在这里处理一些自己的核心业务。");
        adapteeFace.getFaceAlarm();
    }
}
