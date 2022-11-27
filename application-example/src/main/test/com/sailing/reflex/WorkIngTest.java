package com.sailing.reflex;

import com.sailing.application.ExampleApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: es-spring-boot-starter-pom
 * @description:
 * @author: LIULEI
 * @create: 2020-07-31 09:10:
 **/
@Slf4j
@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(classes = ExampleApplication.class)
public class WorkIngTest {

    @Autowired
    private WorkInfo workInfo;

    @Test
    public void testEventExample(){
//        List params = new ArrayList();
        log.info("测试父类实例加载所有子类对象");
    }
}
