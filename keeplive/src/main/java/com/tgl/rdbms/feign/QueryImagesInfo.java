package com.tgl.rdbms.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-07-22 11:25:
 **/
@Component
@FeignClient(name = "server-images",
        url = "http://127.0.0.1:2002")
public interface QueryImagesInfo {

    @RequestMapping(value = "/v1/getImages", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImages();
}
