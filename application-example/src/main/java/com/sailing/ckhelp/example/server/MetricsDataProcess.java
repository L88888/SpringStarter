package com.sailing.ckhelp.example.server;

import com.sailing.ckhelp.example.entity.StatisticsDO;
import com.sit.client.ckhelper.service.ClickHouseData;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @program: spring-starter
 * @description: metrics数据处理
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-17 15:18:
 **/
@Service
@AllArgsConstructor
public class MetricsDataProcess {

    @Autowired
    private ClickHouseData clickHouseData;

    public StatisticsDO dataStatistics(){
        String[][] timeData = new String[17][3];
        timeData[0] = new String[]{"5","2022-05-01 00:00:00","2022-05-01 23:59:59"};
        timeData[1] = new String[]{"4","2022-05-01 00:00:00","2022-05-01 23:59:59"};
        timeData[2] = new String[]{"3","2022-05-01 00:00:00","2022-05-01 23:59:59"};
        timeData[3] = new String[]{"2","2022-05-01 00:00:00","2022-05-01 23:59:59"};
        timeData[4] = new String[]{"1","2022-05-01 00:00:00","2022-05-01 23:59:59"};
        timeData[5] = new String[]{"12","2022-05-01 00:00:00","2022-05-01 23:59:59"};
        timeData[6] = new String[]{"11","2022-05-01 00:00:00","2022-05-01 23:59:59"};
        timeData[7] = new String[]{"10","2022-05-01 00:00:00","2022-05-01 23:59:59"};

        for (int i =0;i < timeData.length;i++){
            // 0 是五月份
            String sql = "SELECT count(1) as title from default.metrics";
            List t1 = clickHouseData.queryStaticsData(sql, StatisticsDO.class);
            if (CollectionUtils.isEmpty(t1)){
                return new StatisticsDO();
            }
            StatisticsDO r1 = (StatisticsDO) t1.get(0);
//            Thread.sleep(2000);
        }

        // update db
        String sql = "SELECT count(1) as title from default.metrics";
        List t1 = clickHouseData.queryStaticsData(sql, StatisticsDO.class);
        if (CollectionUtils.isEmpty(t1)){
            return new StatisticsDO();
        }
        StatisticsDO r1 = (StatisticsDO) t1.get(0);
        return r1;
    }
}
