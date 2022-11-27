package com.sit.client.tests.adapter;

import com.tgl.designpattern.service.adapter.AdapteeFace;
import com.tgl.designpattern.service.adapter.AdapterFace;
import com.tgl.designpattern.service.adapter.Target;
import org.junit.Test;

/**
 * @program: spring-starter
 * @description: 适配器模式测试
 * @author: LIULEI-TGL
 * @create: 2021-07-27 14:18:
 **/
public class AdapterFaceTest {

    @Test
    public void faceTest(){
        Target target = new AdapterFace(new AdapteeFace());
        target.request();
    }
}
