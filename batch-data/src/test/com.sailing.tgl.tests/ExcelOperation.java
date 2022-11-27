package com.sailing.tgl.tests;

import com.alibaba.excel.EasyExcel;
import com.tgl.rdbms.app.RdbmsApplication;
import com.tgl.rdbms.concurrent.TglThreadDataCache;
import com.tgl.rdbms.entity.CheckTransInfoDto;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: spring-starter
 * @description: excel导入数据操作
 * @author: LIULEI-TGL
 * @create: 2021-07-09 18:49:
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RdbmsApplication.class)
@Slf4j
public class ExcelOperation {

    @Test
    public void noModelMultipleSheet() {
        try {
//            try (var ctx = new TglThreadDataCache(false)){
//            }
            String filePath = "F:/platfromWork/SpringStarter/examples-master/spring-starter/tglraft-rocksdb/tempData.xlsx";
            EasyExcel.read(filePath, CheckTransInfoDto.class,
                    new ExcelModelListener()).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void noModelMultiple() {
        log.info("{}", 5999 % 5000);
    }
}

