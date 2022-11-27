package com.tgl.rdbms.service;

import com.tgl.rdbms.fileutils.FileOperation;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-07-22 11:07:
 **/
@Service
public class QueryImagesInfoImpl implements QueryImagesInfo {

    private String faceImagePath = "F:\\doubleSceen\\2.jpg";

    /**
     * 提供图片下载的实现
     * @return 图片的base64数据
     */
    @Override
    public byte[] downImages() {
        return FileOperation.getInstance().readFileMap(new File(faceImagePath));
    }
}
