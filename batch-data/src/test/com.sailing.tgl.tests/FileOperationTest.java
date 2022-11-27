package com.sailing.tgl.tests;

import com.tgl.rdbms.fileutils.FileOperation;
import org.junit.Test;

import java.io.File;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-07-22 14:24:
 **/
public class FileOperationTest {

    @Test
    public void testFileData(){
        String filePath = "F:\\doubleSceen\\2.jpg";
        byte[] t = FileOperation.getInstance().readFileMap(new File(filePath));
        System.out.println(t);
    }
}
