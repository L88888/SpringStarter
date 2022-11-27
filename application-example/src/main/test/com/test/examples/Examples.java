package com.test.examples;

import com.google.common.collect.Lists;
import com.sailing.paralleltask.poolutil.SitTaskProcess;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @program: spring-starter
 * @description: 判断Object对象是不是数组对象Object[]
 * @author: LIULEI
 * @create: 2020-09-19 19:32:
 **/
@Slf4j
public class Examples {

    @Data
    class Bank{
        String name;
        int money;
        Date storageTime;
    }

    @Test
    public void testObjectArrays(){
        if (this.arraysObject().getClass().isArray()){
            System.out.println("数组对象.");
        }else {
            System.out.println("非数组对象.");
        }
    }

    Object arraysObject(){
        Object[] banks = new Object[10];
        Bank bank = new Bank();
        bank.setMoney(123);
        bank.setName("张三");
        bank.setStorageTime(new Date());
        banks[0]= bank;

        Bank bank1 = new Bank();
        bank1.setMoney(123);
        bank1.setName("张三");
        bank1.setStorageTime(new Date());
        banks[1]= bank1;

        Bank bank2 = new Bank();
        bank2.setMoney(123);
        bank2.setName("张三");
        bank2.setStorageTime(new Date());
        banks[2]= bank2;
        return banks;
    }


    /**
     * 对List容器进行拆解分区
     * @return
     */
    @Test
    public void arraysPartition(){
        List dateTemp = new ArrayList(10);
        Object[] banks = new Object[10];
        Bank bank = new Bank();
        bank.setMoney(1234);
        bank.setName("张三1");
        bank.setStorageTime(new Date());
        banks[0]= bank;
        dateTemp.add(banks);

        Bank bank1 = new Bank();
        bank1.setMoney(1235);
        bank1.setName("张三2");
        bank1.setStorageTime(new Date());
        banks[1]= bank1;
        dateTemp.add(bank1);

        Bank bank2 = new Bank();
        bank2.setMoney(1236);
        bank2.setName("张三3");
        bank2.setStorageTime(new Date());
        banks[2]= bank2;
        dateTemp.add(bank2);

        Bank bank3 = new Bank();
        bank3.setMoney(1237);
        bank3.setName("张三4");
        bank3.setStorageTime(new Date());
        banks[2]= bank3;
        dateTemp.add(bank3);

        Bank bank4 = new Bank();
        bank4.setMoney(1238);
        bank4.setName("张三5");
        bank4.setStorageTime(new Date());
        banks[2]= bank4;
        dateTemp.add(bank4);

        // 容器数据分区，底层实现还是sublist()
        List<List<Bank>> resData = Lists.partition(dateTemp,4);
        log.info("resData is values:>>>{}", resData);
        for (List v : resData){
            log.info("子项集合大小:>>>{}", v.size());
            log.info("子项集合数据 is values:>>>{}", v);
        }

        CountDownLatch downLatch = new CountDownLatch(resData.size());
        for (final List<Bank> vb : resData){
            ExecutorService executorService =
                    SitTaskProcess.getOrInitExecutor("liulei-test", 5);
//            executorService.submit(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        log.info("消费到的对象集合大小为:>{}", vb.size());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        downLatch.countDown();
//                    }
//                }
//            });

            if (true){
                executorService.submit(()->{
                    try {
                        log.info("消费到的对象集合大小为:>{}", vb.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        downLatch.countDown();
                    }
                });
            }
        }

        try{
            downLatch.await();
        }catch (InterruptedException e){
            log.info("{}", e.fillInStackTrace());
        }
    }
}
