package com.tgl.designpattern.service.strategy;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @program: spring-starter
 * @description: 批量布控名单上传，并获取人脸导图、车辆号牌数据检索结果
 * @author: LIULEI-TGL
 * @create: 2021-07-27 14:50:
 **/
@Slf4j
public final class BatchTaskImpl implements TaskObject {

    /**
     * 开启多线程的方式批量导入人脸图片至算法库中
     * @param faceData
     * @return
     */
    @Override
    public List<Boolean> upLoadFaceImage(List<Object> faceData) {
        log.info("开启多线程的方式批量导入人脸图片至算法库中,{}", faceData);
        return null;
    }

    @Override
    public List<String> queryNumInfo(List<String> personData) {
        log.info("开启多线程的方式根据人员身份证号，批量获取车辆数据,{}", personData);
        return null;
    }
}
