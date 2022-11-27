package org.test.event;

import com.sailing.application.ExampleApplication;
import com.sailing.event.eventexample.QueryBussData;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: es-spring-boot-starter-pom
 * @description:
 * @author: LIULEI
 * @create: 2020-06-08 16:06:
 **/
@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(classes = ExampleApplication.class)
@RequiredArgsConstructor
public class EventTest {

    private final QueryBussData queryBussData;

    @Test
    public void testEventExample(){
        List params = new ArrayList();
        params.add("生产实时警员gps数据2020-0618");
        queryBussData.onMessage(params);
    }

    @Test
    public void testBitMap(){
        // 定义一个1千万大小的数组，测试内存占用情况
        int numSize = 10000000;
        String[] num= new String[numSize];
        long[] idNum = new long[numSize];
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();

        System.out.printf("1千万大小的数组内存占用情况 %dMB\n",totalMemory - freeMemory);
    }
}
