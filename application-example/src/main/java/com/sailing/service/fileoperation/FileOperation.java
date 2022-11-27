package com.sailing.service.fileoperation;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @program: spring-starter
 * @description: 通过FileChange操作文件流（读文件、写文件）
 * @author: LIULEI
 * @create: 2021-04-05 10:48:
 **/
public class FileOperation {

    /**
     * 两个文件进行内容拷贝，采用文件通道的方式进行拷贝
     * （省去了读文件A，写文件B的麻烦与防止内存oom溢出）
     * @param fromFile  F:\KaiFaGongJu\ideaIU-2017.2.4.exe
     * @param toFile  F:\home\demoFileChannel\ideaIU-2017.2.4.exe
     */
    private void fileCopyWithFileChannel(File fromFile, File toFile){
        // 源文件流对象
        FileInputStream fileInputStream = null;
        // 目标文件流对象
        FileOutputStream fileOutputStream = null;

        // 文件通道
        FileChannel fileChannelInput = null;
        FileChannel fileChannelOutPut = null;

        try {
            fileInputStream = new FileInputStream(fromFile);
            fileOutputStream = new FileOutputStream(toFile);

            fileChannelInput = fileInputStream.getChannel();
            fileChannelOutPut = fileOutputStream.getChannel();
            // 获取源文件长度
            System.out.println("源文件长度为:>" + fileChannelInput.size());

            long stime = System.currentTimeMillis();
            System.out.println("文件"+fromFile.getName()+"拷贝中请等待.");
            // 将fileChannelInput文件流通道中的数据写入，fileChannelOutPut文件流通道中
            long resFileData = fileChannelInput.transferTo(0, fileChannelInput.size(), fileChannelOutPut);
            long etime = System.currentTimeMillis();
            System.out.println("文件拷贝完成,已接收文件大小结果为:>" + resFileData + "文件拷贝耗时:>" + (etime - stime));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                }
                if (fileOutputStream != null){
                    fileOutputStream.close();
                }
                if (fileChannelInput != null){
                    fileChannelInput.close();
                }
                if (fileChannelOutPut != null){
                    fileChannelOutPut.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 将文件内容提供filechannel 的map方法映射至内存
     * @param sourceFile
     */
    private void fileMap(File sourceFile){
        if (sourceFile != null && sourceFile.length() > 0){
            long fileL = sourceFile.length();

            // 获取文件的getChannel通道对象
            try {
                // 开始给文件中写入数据对象
                String writeData = "asdas==========asdasqweqw撒打算打算尽快哈撒旦教" + "\\n";
//                fileL += writeData.getBytes().length;
                // rw 读写, r 读
                MappedByteBuffer mappedByteBuffer = new RandomAccessFile(sourceFile, "rw")
                        .getChannel().map(FileChannel.MapMode.READ_WRITE,0, fileL);

//                mappedByteBuffer.put(writeData.getBytes());
//                mappedByteBuffer.put(writeData.getBytes());
//                mappedByteBuffer.put(writeData.getBytes());
                mappedByteBuffer.put("儿童与is啊啊是123123123".getBytes());
                mappedByteBuffer.force();
                mappedByteBuffer.flip();

                // 得到缓冲区的容量
                int cacheCapacity = mappedByteBuffer.capacity();
                byte[] fileds = new byte[cacheCapacity];
                // 开始读取缓存中的文件内容byte
                // hasRemaining() 获取文件内容中当前位置与文件极限（最大位置）位置
                // position() 获取缓冲区的位置
                while (mappedByteBuffer.hasRemaining()){
                    fileds[mappedByteBuffer.position()] = mappedByteBuffer.get();
                }

                // 扫描缓存中的数据对象，输出对象数据
                Scanner scanner = new Scanner(new ByteArrayInputStream(fileds)).useDelimiter(" ");
                while (scanner.hasNext()){
//                    scanner.next();
                    System.out.println(new String(scanner.next().getBytes(), Charset.forName("UTF-8")));
                }

                scanner.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        FileOperation fileOperation = new FileOperation();

//        fromFile  F:\KaiFaGongJu\ideaIU-2017.2.4.exe
//        toFile  F:\home\demoFileChannel\ideaIU-2017.2.4.exe
        // F:\KaiFaGongJu\Office 2010\Visio2010(32_64cn).iso
        File fromFile = new File("F:\\KaiFaGongJu\\Office 2010\\Visio2010(32_64cn).iso");
        File toFile = new File("F:\\home\\demoFileChannel\\Visio2010(32_64cn).iso");
        if (false){
            fileOperation.fileCopyWithFileChannel(fromFile, toFile);
        }

        File sourceFile = new File("F:\\home\\demoFileChannel\\filemap\\301road.txt");
        fileOperation.fileMap(sourceFile);
    }
}
