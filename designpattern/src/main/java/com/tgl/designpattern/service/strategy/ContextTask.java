package com.tgl.designpattern.service.strategy;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @program: spring-starter
 * @description: 策略模式集中调度类；
 *               优点：
 *               1、策略模式结构简单、处理思路清晰;
 *               2、解决了各种if|else条件判断，提升了功能模块的质量;
 *               3、实际当中某一个策略发生变化不会影响其他的策略业务逻辑;
 * @author: LIULEI-TGL
 * @create: 2021-07-27 15:01:
 **/
@Slf4j
public class ContextTask {

    private TaskObject taskObject;

    /**
     * 初始化接口对象
     * @param taskObject
     */
    public ContextTask(TaskObject taskObject){
        this.taskObject = taskObject;
    }

    /**
     * 上传人脸图片至算法库中
     * @param faceData
     * @return
     */
    public List<Boolean> upLoadFaceImage(List<Object> faceData){
        if (Objects.isNull(faceData) || faceData.size() == 0){
            log.info("人脸对象为空，无法导图给人脸算法库数据:>{}", faceData);
            return Collections.emptyList();
        }
        return taskObject.upLoadFaceImage(faceData);
    }

    /**
     * 通过人员身份信息获取对应的车辆数据对象
     * @param personData
     * @return
     */
    public List<String> queryNumInfo(List<String> personData){
        if (Objects.isNull(personData) || personData.size() == 0){
            log.info("人员对象为空，无法关联车辆数据:>{}", personData);
            return Collections.emptyList();
        }
        return taskObject.queryNumInfo(personData);
    }
}
