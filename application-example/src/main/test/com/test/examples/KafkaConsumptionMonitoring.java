package com.test.examples;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class KafkaConsumptionMonitoring {

    private static HashMap<String, AtomicInteger> count = new HashMap<>();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /**
     * 按天对消费的业务数据进行统计分析
     * @param dateVal
     * @param recordNum
     */
    private void consumptionMonitoring(String dateVal, int recordNum){
        if (count.containsKey(dateVal)){
            // 获取计数器对象的引用,然后做数据累加
            count.get(dateVal).getAndAdd(recordNum);
            log.info("当前计数器:{},的大小为:>{}", dateVal, count.get(dateVal).get());
        }else {
            // 按天创建一个计数器对象AtomicInteger
            count.put(dateVal, new AtomicInteger());
        }
    }

    public HashMap getCount(){
        return count;
    }

    @Test
    public void t1(){
        String dateVal = "2022-10-18";
        int recordNum = 122;

        String dateVal1 = "2022-10-19";
        int recordNum1 = 22;

        for (int i = 0;i<20000;i++){
            new Thread(()->{
                String curDate = sdf.format(new Date());
                consumptionMonitoring(curDate, recordNum);

                consumptionMonitoring(curDate, recordNum1);
            }).start();
        }
    }
}
