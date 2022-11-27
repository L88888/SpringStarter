package com.sit.personcar.track.models.utils;

import org.springframework.lang.Nullable;

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

    public static final String SERVICEID = "serviceId";

    public static final String STATUS = "-1";

    public static final int STATUSCODE = -1;

    public static final String SERVICE_ID = "service_id";

    public static final String SID = "sid";

    public static final String KEYSFG = "@";

    public static final String REGISTER = "1";

    /**
     * 字符串非空判断
     * @param str
     * @return
     */
    public static boolean isEmpty(@Nullable Object str) {
        return (str == null || "".equals(str));
    }
}