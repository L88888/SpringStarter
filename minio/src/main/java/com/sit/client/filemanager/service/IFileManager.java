package com.sit.client.filemanager.service;

/**
 * @program: spring-starter
 * @description: 文件托管接口
 * @author: LIULEI-TGL[知行合一]
 * @create: 2021-11-24 11:02:最终托管至文件存储服务minio中
 **/
public interface IFileManager {
    public String fileUpload(String filePath,String fileName);
}
