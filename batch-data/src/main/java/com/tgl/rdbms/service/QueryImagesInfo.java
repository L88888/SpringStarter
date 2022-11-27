package com.tgl.rdbms.service;

import org.springframework.stereotype.Service;

/**
 * @program: spring-starter
 * @description:下载图片base64结果值
 * @author: LIULEI-TGL
 * @create: 2021-07-22 11:05:
 **/
@Service
public interface QueryImagesInfo {

    /**
     * 下载图片
     * @return
     */
    public byte[] downImages();
}
