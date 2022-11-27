package com.tgl.rdbms.fileutils;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-07-22 11:41:
 **/
public class FileOperation {

    private volatile static FileOperation fileOperation;

    private FileOperation(){}

    public static FileOperation getInstance(){
        if (null == fileOperation){
            synchronized (FileOperation.class){
                if (null == fileOperation){
                    fileOperation = new FileOperation();
                }
            }
        }
        return fileOperation;
    }


    /**
     * 将文件内容通过filechannel 的map方法映射至内存
     * @param sourceFile
     */
    public void fileMap(File sourceFile, byte[] fileData){
        if (sourceFile != null && fileData.length > 0){
            // 获取文件的getChannel通道对象
            try {
                if (!sourceFile.exists()){
                    sourceFile.createNewFile();
                }
                long fileL = fileData.length;

                // rw 读写, r 读, w 写
                MappedByteBuffer mappedByteBuffer = new RandomAccessFile(sourceFile, "rw")
                        .getChannel().map(FileChannel.MapMode.READ_WRITE,0, fileL);
                mappedByteBuffer.clear();
                mappedByteBuffer.put(fileData);
                mappedByteBuffer.force();
                mappedByteBuffer.flip();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
