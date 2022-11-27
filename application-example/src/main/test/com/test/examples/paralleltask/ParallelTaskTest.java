package com.test.examples.paralleltask;

import com.sailing.paralleltask.task.SitTaskLoop;
import org.junit.Test;

/**
 * @ClassName ParallelTaskTest
 * @Description TODO
 * @Author Liulei
 * @Date 2022/11/25 18:39
 * @Version 1.0
 **/
public class ParallelTaskTest {

    @Test
    public void t1(){
        SitTaskLoop sitTaskLoop = new SitTaskLoop();
        sitTaskLoop.startTask();

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sitTaskLoop.stopTask();
    }
}
