package com.sailing.reflex.threadlocal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @program: es-spring-boot-starter-pom
 * @description:
 * @author: LIULEI
 * @create: 2020-08-04 11:33:
 **/
@RunWith(SpringJUnit4ClassRunner.class)
public class SThreadLocalTest {

    @Test
    public void testThreadLocaData(){
        SThreadLocal sThreadLocal = new SThreadLocal();
        boolean t = false;
        if (t){
            sThreadLocal.connSession();
        }

        int num = 100;
        int capacity = 100;
        sThreadLocal.attemptPrint(num, capacity);
    }

    @Test
    public void tt1(){
        System.out.println("ssssssssss-------");
    }
}
