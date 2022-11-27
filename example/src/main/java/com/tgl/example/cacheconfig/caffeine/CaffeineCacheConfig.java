package com.tgl.example.cacheconfig.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: spring-starter
 * @description: caffeine cache动态配置
 * Caffeine配置说明
    initialCapacity=[integer]: 初始的缓存空间大小
    maximumSize=[long]: 缓存的最大条数
    maximumWeight=[long]: 缓存的最大权重
    expireAfterAccess=[duration]: 最后一次写入或访问后经过固定时间过期
    expireAfterWrite=[duration]: 最后一次写入后经过固定时间过期
    refreshAfterWrite=[duration]: 创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存
    weakKeys: 打开key的弱引用
    weakValues：打开value的弱引用
    softValues：打开value的软引用
    recordStats：开发统计功能
    注意
    expireAfterWrite和expireAfterAccess同事存在时，以expireAfterWrite为准。
    maximumSize和maximumWeight不可以同时使用
    weakValues和softValues不可以同时使用
 * @author: LIULEI-TGL
 * @create: 2021-09-26 13:41:
 **/
@Configuration
public class CaffeineCacheConfig {

    /**
     * 定义每一种缓存类型自己的消亡时间，单位为秒
     * initialCapacity=50,maximumSize=500,expireAfterWrite=5s
     * @return
     */
    @Primary
    @Bean
    public CacheManager caffeineCacheManager(){
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        List<CaffeineCache> caffeineCaches = new ArrayList<>();
        for (CacheType cacheType : CacheType.values()) {
            caffeineCaches.add(new CaffeineCache(cacheType.name(),
                    Caffeine.newBuilder()
                            .initialCapacity(50)
                            .maximumSize(500)
                            .expireAfterWrite(cacheType.getExpires(), TimeUnit.SECONDS)
                            .build()));
        }
        simpleCacheManager.setCaches(caffeineCaches);
        return simpleCacheManager;
    }
}
