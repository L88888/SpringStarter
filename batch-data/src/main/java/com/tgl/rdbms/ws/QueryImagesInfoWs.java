package com.tgl.rdbms.ws;

import com.tgl.rdbms.service.QueryImagesInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-07-22 11:10:
 **/
@RestController
public class QueryImagesInfoWs {

    @Autowired
    private QueryImagesInfo queryImagesInfoImpl;

    /**
     * 获取本地图片base64流对象
     * http://127.0.0.1:2002/v1/getImages
     * @return
     */
    @RequestMapping(value = "v1/getImages", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImages() {
        return queryImagesInfoImpl.downImages();
    }
}
