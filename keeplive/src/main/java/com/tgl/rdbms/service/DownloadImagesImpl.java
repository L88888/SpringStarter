package com.tgl.rdbms.service;

import com.tgl.rdbms.concurrent.TglRaftThreadHelper;
import com.tgl.rdbms.concurrent.TglRaftThreadPool;
import com.tgl.rdbms.feign.QueryImagesInfo;
import com.tgl.rdbms.fileutils.FileOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-07-22 11:19:
 **/
@Service
@Slf4j
public class DownloadImagesImpl implements DownloadImages {

    @Autowired
    private QueryImagesInfo queryImagesInfo;

    /**
     * 记录失败的线程对象
     */
    private ConcurrentLinkedQueue<Runnable> failQueue = new ConcurrentLinkedQueue<>();

    /**
     * 默认下载图片数量10000
     */
    private int faceImagesNum = 10000;

    private CountDownLatch faceLatch = new CountDownLatch(faceImagesNum);

    private AtomicInteger imagesNum = new AtomicInteger(0);

    LongAdder adder = new LongAdder();

    /**
     * 开启多线程下载并保持人脸图片
     */
    @Override
    public void getImages() {
        try {
            for (int i = 0;i < faceImagesNum;i++){
                TglRaftThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            byte[] faceImagesByte = queryImagesInfo.getImages();
                            String faceImagePath = "F:\\doubleSceen\\poolthread\\";
                            // 开启mmap写入本地磁盘
                            faceImagePath = faceImagePath.concat(String.valueOf(imagesNum.incrementAndGet())).concat(".jpg");
                            FileOperation.getInstance().fileMap(new File(faceImagePath), faceImagesByte);
                            log.info("本地存储的人脸图片路径:>{}", faceImagePath);
                            // 限流
                            faceLatch.countDown();
                        } catch (Exception e) {
                            log.info("线程内部异常：>{}", e.fillInStackTrace());
                            failQueue.offer(this);
                        }
                    }
                });
            }

            faceLatch.await();
            log.info("已下载图片数量:>{}", imagesNum.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定时器一秒一次开始消费
     */
//    @Scheduled(cron = "*/1 * * * * ?")
    public void consumerFailQueue(){
        boolean resData = false;
        while (!resData) {
            if (failQueue.size() > 0){
                // 继续消费图片下载队列数据
                failQueue.poll().run();
            }else {
                resData = true;
            }
            TglRaftThreadHelper.sleep(500);
        }
    }
}
