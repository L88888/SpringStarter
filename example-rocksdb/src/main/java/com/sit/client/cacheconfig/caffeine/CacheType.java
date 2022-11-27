package com.sit.client.cacheconfig.caffeine;

/**
 * @program: spring-starter
 * @description: 缓存类型，根据不同的缓存类型来定义每个缓存类型自己的消亡时间，单位秒
 * @author: LIULEI-TGL
 * @create: 2021-09-26 14:20:
 **/
public enum CacheType {
    USER(5),
    TENANT(20),
    // 设备业务数据缓存
    cacheDevice(500);

//    ycyd(3600);

    private int expires;

    CacheType(int expires) {
        this.expires = expires;
    }

    public int getExpires() {
        return expires;
    }
}
