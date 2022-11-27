package com.test.examples.semaphore;

import com.sailing.service.semaphore.SemaphoreTemp;
import org.junit.Test;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI
 * @create: 2021-03-02 10:24:
 **/
public class Examples01 {

    @Test
    public void carTest(){
        SemaphoreTemp semaphoreTemp = new SemaphoreTemp();
        semaphoreTemp.carOIfTest();
    }
}
