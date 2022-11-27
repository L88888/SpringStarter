package com.sit.client.tests.strategy;

import com.tgl.designpattern.service.strategy.BatchTaskImpl;
import com.tgl.designpattern.service.strategy.ContextTask;
import com.tgl.designpattern.service.strategy.ExternalTaskImpl;
import com.tgl.designpattern.service.strategy.TaskObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: spring-starter
 * @description: 策略模式测试
 * @author: LIULEI-TGL
 * @create: 2021-07-27 15:24:
 **/
public class TaskTest {

    @Test
    public void testStrategy(){
        TaskObject taskObject = new BatchTaskImpl();
        ContextTask contextTask = new ContextTask(taskObject);
        List<String> personData = new ArrayList<>();
        personData.add("123123123");
        personData.add("456456456");
        personData.add("134567890");
        personData.add("098765432");
        contextTask.queryNumInfo(personData);
        List<Object> faceDatas = new ArrayList<>();
        faceDatas.add("face1");
        faceDatas.add("face2");
        faceDatas.add("face3");
        faceDatas.add("face4");
        contextTask.upLoadFaceImage(faceDatas);

        // 第三方扩展任务实现
        taskObject = new ExternalTaskImpl();
        contextTask = new ContextTask(taskObject);
        List<String> externalPersonData = new ArrayList<>();
        externalPersonData.add("123123123X");
        externalPersonData.add("456456456X");
        externalPersonData.add("134567890X");
        externalPersonData.add("098765432X");
        contextTask.queryNumInfo(externalPersonData);
        List<Object> faceDataTos = new ArrayList<>();
        faceDataTos.add("faces5");
        faceDataTos.add("faces6");
        faceDataTos.add("faces7");
        faceDataTos.add("faces8");
        contextTask.upLoadFaceImage(faceDataTos);
    }
}
