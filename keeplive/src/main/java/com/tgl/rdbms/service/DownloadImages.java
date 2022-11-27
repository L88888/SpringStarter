package com.tgl.rdbms.service;

import org.springframework.stereotype.Service;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-07-22 11:17:
 **/
@Service
public interface DownloadImages {

    /**
     * 获取下载的图片并写入本地磁盘中,开启多线程同时下载写入
     */
    public void getImages();
}
