package com.tgl.designpattern.service.strategy;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @program: spring-starter
 * @description: 外部系统扩展实现
 * @author: LIULEI-TGL
 * @create: 2021-07-27 14:53:
 **/
@Slf4j
public final class ExternalTaskImpl implements TaskObject {
    @Override
    public List<Boolean> upLoadFaceImage(List<Object> faceData) {
        log.info("第三方扩展实现,人脸图片导入人脸算法库{}", faceData);
        return null;
    }

    @Override
    public List<String> queryNumInfo(List<String> personData) {
        log.info("第三方扩展实现,根据人员身份证号获取所属车辆信息{}", personData);
        return null;
    }
}
