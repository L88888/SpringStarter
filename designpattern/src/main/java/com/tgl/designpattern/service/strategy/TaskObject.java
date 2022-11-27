package com.tgl.designpattern.service.strategy;

import java.util.List;

/**
 * @program: spring-starter
 * @description: 定义布控任务主体接口，依图人脸上传、治理平台获取车辆信息[根据身份证号码获取]
 * @author: LIULEI-TGL
 * @create: 2021-07-27 14:44:
 **/
public interface TaskObject {

    /**
     * 上传人脸图片至算法库中
     * @param faceData
     * @return
     */
    public List<Boolean> upLoadFaceImage(List<Object> faceData);

    /**
     * 通过人员身份信息获取对应的车辆数据对象
     * @param personData
     * @return
     */
    public List<String> queryNumInfo(List<String> personData);
}
