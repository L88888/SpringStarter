package com.sailing.tgl.test;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.junit.Test;

/**
 * @program: spring-starter
 * @description: excel导入数据操作
 * @author: LIULEI-TGL
 * @create: 2021-07-09 18:49:
 **/
@Slf4j
public class ExcelOperation {

    @Test
    public void noModelMultipleSheet() {
        try {
            try (var ctx = new ThreadDataCache(false)){
                String filePath = "F:/platfromWork/SpringStarter/examples-master/spring-starter/tglraft-rocksdb/tempData.xlsx";
                EasyExcel.read(filePath, CheckTransInfoDto.class,
                        new ExcelModelListener()).sheet().doRead();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void noModelMultiple() {
        log.info("{}", 5999 % 5000);
    }
}

