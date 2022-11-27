package com.sit.client.tests;

import com.sit.client.ckhelper.entity.Statistics;
import com.sit.client.ckhelper.entity.User;
import com.sit.client.ckhelper.service.ClickHouseData;
import org.junit.Test;

import java.util.*;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-14 20:01:
 **/
public class TestCk {

    /**
     * 翡冷翠
     * @param ager
     */
    public static void main(String[] ager){
        ClickHouseData clickHouseData = new ClickHouseData();
        new Thread(()->{
            String sql = "SELECT count(1) as title from default.metrics";
            List t1 = clickHouseData.queryStaticsData(sql, Statistics.class);
            System.out.println("tttttttttttt1::::" + t1);
        }).start();

        new Thread(()-> {
            String queryMetrics = "SELECT * from default.metrics " +
                    "where 1=1 LIMIT 10";
            List t2 = clickHouseData.queryData(queryMetrics, User.class);
            System.out.println("tttttttttttt2222::::" + t2.size());
        }).start();
    }

    @Test
    public void dataStatistics(){
        ClickHouseData clickHouseData = new ClickHouseData();
        String sql = "SELECT count(1) as title from default.metrics";
        List t1 = clickHouseData.queryStaticsData(sql, Statistics.class);
        if (Objects.isNull(t1)){
            System.out.println("为查询到业务数据::::" + t1);
            return;
        }
        Statistics r1 = (Statistics) t1.get(0);
        System.out.println("统计数据值::::" + r1.getTitle());
    }

    /**
     * slogan
     */
    @Test
    public void batchDataAdd(){
        ClickHouseData clickHouseData = new ClickHouseData();

        // Insert data
        List<Object[]> rows = new ArrayList<>(10000);
        for (int i =0;i < 10000;i++){
            rows.add(generateRow());
        }

        String sql = "INSERT INTO default.metrics";
        String t1 = clickHouseData.batchInsertRecord(sql, rows);
        System.out.println("添加业务数据后，得到的结果为::::" + t1);
    }

    @Test
    public void singleDataAdd(){
        ClickHouseData clickHouseData = new ClickHouseData();

        String sql = "INSERT INTO default.metrics";
        String t1 = clickHouseData.insertRecord(sql, generateRow());
        System.out.println("添加业务数据后，得到的结果为::::" + t1);
    }

    /**
     * 通过指定分区来删除该分区下的业务数据
     * 删除业务数据时不太建议通过业务key或者主键数据来进行删除
     */
    @Test
    public void dropRecordData(){
        ClickHouseData clickHouseData = new ClickHouseData();

        String sql = "ALTER TABLE default.metrics DROP PARTITION '1649998963683'";
        String t1 = clickHouseData.dropPartitionRecord(sql);
        System.out.println("删除分区数据，得到的结果为::::" + t1);
    }

    /**
     * 封装业务数据集合
     * @return
     */
    public static Object[] generateRow() {
        User userData = new User(UUID.randomUUID(),
                "metric_1",
                System.currentTimeMillis(),
                new Random().nextDouble());

        return new Object[] {userData.getDeviceId(),
                userData.getMetric(),
                userData.getTime(),
                userData.getValue()};
    }
}
