package com.tgl.rdbms.fileutils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

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
     * 将文件内容提供filechannel 的map方法映射至内存
     * @param sourceFile
     */
    public void readFileMap(File sourceFile, byte[] fileData){
        if (sourceFile != null && sourceFile.length() > 0){
            try {

                long fileLength = sourceFile.length();
                final int BUFFER_SIZE=60;

                //此种方式内存占用将稳定在20M
                MappedByteBuffer inputBuffer = new RandomAccessFile(sourceFile,"r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, fileLength);

                byte[] dst = null;
                List<Byte> dst2 = null;
                int offset = 0;
                int s = 0;
                do{
                    dst = new byte[BUFFER_SIZE];
                    dst2 = new ArrayList<Byte>();
                    for(int a=0;a<BUFFER_SIZE;a++){
                        dst[a] = inputBuffer.get(offset);
                        offset ++;
                    }

                    String d1 = new String(dst,"UTF-8");
                    if(!d1.endsWith("1460944800}")){
                        for(int b = 0;b<2048;b++){
                            byte by = inputBuffer.get(offset);
                            dst2.add(by);
                            offset++;
                            if(by == '\n'){
                                s++;
                                String d2 = new String(list2Array(dst2),"UTF-8");
                                System.out.println(s+"  "+d1+" --> "+d2);
                                break;
                            }
                        }
                    }
                }while(offset<fileLength);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] list2Array(List<Byte> blist){
        byte[] barr = new byte[blist.size()];
        for(int i=0;i<blist.size();i++){
            barr[i] = blist.get(i);
        }
        return barr;
    }

    /**
     * 获取本地图片流程信息
     * @param sourceFile
     * @return
     */
    public byte[] readFileMap(File sourceFile){
        if (sourceFile != null && sourceFile.length() > 0){
            RandomAccessFile randomAccessFile = null;
            FileChannel fileChannel = null;
            MappedByteBuffer inputBuffer = null;

            try {
                randomAccessFile = new RandomAccessFile(sourceFile,"r");
                fileChannel = randomAccessFile.getChannel();

                long fileLength = fileChannel.size();

                //此种方式内存占用将稳定在20M
                inputBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileLength);
                byte[] bytes = new byte[(int) fileLength];
                for (int i =0; i < fileLength;i++ ){
                    bytes[i] = inputBuffer.get(i);
                }
                return bytes;
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (randomAccessFile != null){
                        randomAccessFile.close();
                    }

                    if (fileChannel != null){
                        fileChannel.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputBuffer = null;
            }
        }

        return null;
    }
}
