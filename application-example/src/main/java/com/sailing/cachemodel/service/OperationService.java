package com.sailing.cachemodel.service;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableRunnable;
import com.sailing.cachemodel.entity.Command;
import com.sailing.cachemodel.entity.LogEntry;
import com.sailing.cachemodel.rocksdb.DefaultLogEntryImpl;
import com.sailing.linkstrack.bo.ThirdPartyLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-02 11:12:
 **/
@Service
@Slf4j
public class OperationService {

    /**
     * 存储日志数据至缓存rocksdb中
     * @param thirdPartyLog
     */
    public void logDataWrite(ThirdPartyLog thirdPartyLog){
        DefaultLogEntryImpl.getInstance().write(thirdPartyLog);
    }

    /**
     * 生产20万样本数据
     * @param thirdPartyLog
     * @param mock10w
     */
    public void logDataWrite(ThirdPartyLog thirdPartyLog,int mock10w){
        String name = thirdPartyLog.getName();
        String id = thirdPartyLog.getId();

        // 2000万数据存储rocksdb测试存储占用情况
        int num = 2 * 1000 * 10000;
        for (int i = 0;i < num;i++){
            String nameT1 = name + i;
            String idT1 = id + i;
            thirdPartyLog.setName(nameT1);
            thirdPartyLog.setId(idT1);

            if (i % 10000 == 0){
                System.out.println("10000>::"+ thirdPartyLog.getId() + thirdPartyLog.getName());
            }
            DefaultLogEntryImpl.getInstance().write(thirdPartyLog,true);
        }
    }

    public void logBatchDataWrite(ThirdPartyLog thirdPartyLog){
        String name = thirdPartyLog.getName();
        String id = thirdPartyLog.getId();
        int num = 200000;
        CountDownLatch downLatch = new CountDownLatch(num);
        for (int i = 0;i < num;i++){
            String nameT1 = name + i;
            String idT1 = id + i;
            new Thread(()->{
                thirdPartyLog.setName(nameT1);
                thirdPartyLog.setId(idT1);
                DefaultLogEntryImpl.getInstance().write(thirdPartyLog);
                downLatch.countDown();
            }).start();
        }

        try {
            downLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取日志数据至缓存rocksdb中
     * @param logId
     */
    public ThirdPartyLog getLogData(String logId){
        return DefaultLogEntryImpl.getInstance().read(logId);
    }
}
