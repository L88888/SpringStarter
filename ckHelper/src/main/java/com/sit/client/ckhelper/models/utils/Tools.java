package com.sit.client.ckhelper.models.utils;

import org.asynchttpclient.Request;
import org.springframework.lang.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ckHelper
 * @description: 处理一些字符串与对象数据判断
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-17 13:24:
 **/
public class Tools {

    /**
     * 操作成功
     */
    public static final String SUCCESS = "success";

    /**
     * 操作失败
     */
    public static final String FAIL = "fail";

    /**
     * 字符串非空判断
     * @param str
     * @return
     */
    public static boolean isEmpty(@Nullable Object str) {
        return (str == null || "".equals(str));
    }

    /**
     * 数据序列化操作，多个数据拼接
     * @param data 对象集合
     * @return
     */
    public static String tabSeparatedString(List<Object[]> data) {
        return data.stream().map(row -> Arrays.stream(row)
                .map(col -> col.toString())
                .collect(Collectors.joining("\t")))
                .collect(Collectors.joining("\n"));
    }

    /**
     * 请求异常时对请求地址进行url编码操作
     * @param request 请求对象
     * @return
     */
    public static String decodedUrl(Request request) {
        final String url = request.getUrl();

        try {
            return URLDecoder.decode(url, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }
}
