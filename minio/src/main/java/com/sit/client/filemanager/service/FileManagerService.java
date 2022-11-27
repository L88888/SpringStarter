package com.sit.client.filemanager.service;

import io.minio.*;
import org.springframework.stereotype.Service;

/**
 * @program: spring-starter
 * @description: 文件上传托管至minio存储服务中
 * @author: LIULEI-TGL[知行合一]
 * @create: 2021-11-24 11:00:
 **/
@Service
public class FileManagerService implements IFileManager{

    /**
     * minio本地服务端访问地址
     */
    private String endpoint = "http://182.168.80.20:19892";
    /**
     * {"console":[{"access_key":"N7B2XV3UIBH0YL9F9BCC","secret_key":"U4bsAxV38ZaHj62CHMZZzFKI+V0YqHeXfW8DqvIE"}]}
     */
    private String accessKey = "N7B2XV3UIBH0YL9F9BCC";
    private String secretKey = "U4bsAxV38ZaHj62CHMZZzFKI+V0YqHeXfW8DqvIE";

    /**
     * 上传至远端minio服务器
     */
    private void reload(){
        if (false){
            return;
        }
        endpoint = "http://172.20.32.192:10278";
        accessKey = "B0A9TOB1JDSDHZZV6JP1";
        secretKey = "A2ERtdWoQSbD7R9SJ+GuqHBg7Wdp+5aHcj6MjqdO";
    }

    /**
     * 本地文件上传至远端文件服务器minio中
     * @param filePath
     * @param fileName
     * @return
     */
    @Override
    public String fileUpload(String filePath, String fileName) {
        String uploadFile = "";

        try {
            reload();
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            // https://182.168.80.20:19893
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();

            String bucketName = "bucket-ceshi007";

            // 检查存储桶是否已经存在
            boolean isExist = false;
            try {
                isExist = minioClient.bucketExists(
                        BucketExistsArgs.builder().bucket(bucketName).build()
                );
            } catch (Exception e) {
                System.out.println("存储桶是否已创建检测异常,异常信息为:>>" + e);
            }

            if(isExist) {
                System.out.println("存储桶已经存在,不需要在重复创建.");
            } else {
                System.out.println("存储桶不存在,开始创建存储桶." + bucketName);
                // 创建一个名为asiatrip的存储桶，用于存储数据文件。
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
            }

            String waitFilePath = "/home/陕A10002.jpg";
            String storgeFilePath = "/2021/11/25/19/50/陕A1000212311.jpg";
            // 使用putObject上传一个文件到存储桶中。
            // /admin123/112/34/we/Pictures.zip
            ObjectWriteResponse objectWriteResponse =
            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(storgeFilePath)
                    .filename(waitFilePath)
                    .build());

            uploadFile = endpoint.concat("/").concat(bucketName).concat("/").concat(storgeFilePath);
            System.out.println("上传文件返回报文信息:>>>" + objectWriteResponse.toString());
            System.out.println("/home/setup.exe is successfully uploaded as setup.exe to `"+bucketName+"` bucket.");
            System.out.println("上传文件访问地址信息:>>>" + uploadFile);
        } catch(Exception e) {
            System.out.println("Error occurred: " + e);
        }
        return uploadFile;
    }
}
