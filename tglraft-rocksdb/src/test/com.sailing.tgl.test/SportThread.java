package com.sailing.tgl.test;

import com.tgl.raftclient.concurrent.TglRaftThread;
import com.tgl.raftclient.concurrent.TglRaftThreadPool;
import com.tgl.raftclient.concurrent.TglRaftThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @program: spring-starter
 * @description: 多线程运动；运动员等待裁判
 * @author: LIULEI-TGL
 * @create: 2021-07-08 16:00:
 **/
@Slf4j
public class SportThread {

    // 等待裁判发枪
    CountDownLatch cp = new CountDownLatch(1);
    // 六个队员开始起跑
    CountDownLatch startSport = new CountDownLatch(10);

    @Test
    public void testSport(){
        for (int i =0; i < 8;i++){
            final int index = i;
            TglRaftThreadPool.execute(()->{
                try {
                    log.info("运动员:>{}正在等待裁判发枪...", index);
                    cp.await();
                    log.info("运动员:>{}已经接收到裁判的发枪...", index);

                    // 处理跑步的业务逻辑
                    TglRaftThread.sleep((long) Math.random() * 2000);
                    log.info("运动员:>{}已经跑到终点.", index);
                    startSport.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        try {
//            TglRaftThread.sleep((long) Math.random() * 5000);
            // 等待裁判发枪
            log.info("裁判准备开始发枪...");
            cp.countDown();
            log.info("裁判已经发枪，等待所有的运动员到达终点...");

            // 等待运动员全部比赛结束
            startSport.await(1000, TimeUnit.SECONDS);
            log.info("运动员全部已经到达终点，比赛结束..");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TglRaftThreadPool.shutdown();
    }
}
