package com.sit.client.tests;

import com.sailing.extractiontool.metadata.QueryFileMetaData;
import org.junit.Test;

import java.io.File;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL[知行合一]
 * @create: 2021-12-29 17:50:
 **/
public class GetFileMetaData {

    @Test
    public void getFileMeta(){
        try {
            File file = new File("D:\\研发内部工作\\刘磊\\02-工程项目\\线索协查分析系统\\metatest.jpg");
            QueryFileMetaData.readImageInfo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
