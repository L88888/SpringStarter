package com.ckhelp;

import com.sailing.ckhelp.example.entity.StatisticsDO;
import com.sailing.ckhelp.example.entity.UserDO;
import com.sit.client.ckhelper.service.CkClient;
import com.sit.client.ckhelper.service.ClickHouseData;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @program: ckHelper
 * @description: API接口测试用例
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-14 20:01:
 **/
public class TestCk {
    private static final Logger LOG = LoggerFactory.getLogger(TestCk.class);

    private static CkClient clickHouseData = new ClickHouseData();

    /**
     * 翡冷翠
     * @param ager
     */
    public static void main(String[] ager){
        new Thread(()->{
            String sql = "SELECT count(1) as title from default.metrics";
            List t1 = clickHouseData.queryStaticsData(sql, StatisticsDO.class);
            LOG.info("tttttttttttt1::::>{}", t1);
        }).start();

        new Thread(()-> {
            String queryMetrics = "SELECT * from default.metrics " +
                    "where 1=1 LIMIT 10";
            List t2 = clickHouseData.queryData(queryMetrics, UserDO.class);
            LOG.info("tttttttttttt2222::::>{}", t2.size());
        }).start();
    }

    /**
     * 获取业务数据统计
     */
    @Test
    public void dataStatistics(){
        String sql = "SELECT count(1) as title from default.metrics";
        List t1 = clickHouseData.queryStaticsData(sql, StatisticsDO.class);
        if (Objects.isNull(t1)){
            LOG.info("为查询到业务数据::::>{}", t1);
            return;
        }
        StatisticsDO r1 = (StatisticsDO) t1.get(0);
        LOG.info("统计数据值::::>{}", r1.getTitle());
    }

    /**
     * slogan
     * 批量业务数据
     */
    @Test
    public void batchDataAdd(){
        // mock新增业务数据
        List<Object[]> rows = new ArrayList<>(10000);
        for (int i =0;i < 10000;i++){
            rows.add(generateRow());
        }

        String sql = "INSERT INTO default.metrics";
        String t1 = clickHouseData.batchInsertRecord(sql, rows);
        LOG.info("添加业务数据后，得到的结果为::::>{}" + t1);
    }

    /**
     * 单条业务数据
     */
    @Test
    public void singleDataAdd(){
        String sql = "INSERT INTO default.metrics";
        String t1 = clickHouseData.insertRecord(sql, generateRow());
        LOG.info("添加业务数据后，得到的结果为::::>{}" + t1);
    }

    @Test
    public void execSql(){
        // mock新增业务数据
        String sql = "insert into default.metrics_shadow (device_id,\n" +
                "metric,`time`,value) select device_id,\n" +
                "metric,`time`,value from default.metrics";
        String t1 = clickHouseData.execDataOperation(sql);
        LOG.info("执行sql后，得到的结果为::::>{}" + t1);
    }

    /**
     * 通过指定分区来删除该分区下的业务数据
     * 删除业务数据时不太建议通过业务key或者主键数据来进行删除
     */
    @Test
    public void dropRecordData(){
        String sql = "ALTER TABLE default.metrics DROP PARTITION '1649998963683'";
        String t1 = clickHouseData.dropPartitionRecord(sql);
        LOG.info("删除分区数据，得到的结果为::::>{}" + t1);
    }

    /**
     * 封装业务数据集合，mock
     * @return
     */
    public static Object[] generateRow() {
        UserDO userData = new UserDO(UUID.randomUUID(),
                "metric_1",
                System.currentTimeMillis(),
                new Random().nextDouble());

        return new Object[] {userData.getDeviceId(),
                userData.getMetric(),
                userData.getTime(),
                userData.getValue()};
    }
}
