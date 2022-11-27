package com.test.examples.dispeldoubts;

import java.io.*;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-01-20 16:23:
 **/
public class DispelDoubts1 {

    /**
     * 文件流通道安全关闭方法
     */
    private void copyFile(){
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream("");
            out = new FileOutputStream("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            this.closeIgnoringException(in);
            this.closeIgnoringException(out);
        }
    }

    /**
     * 公共方法关闭文件流通道
     * @param closeable
     */
    private void closeIgnoringException(Closeable closeable){
        if (closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
