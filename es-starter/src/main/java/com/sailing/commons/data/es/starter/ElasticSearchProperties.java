package com.sailing.commons.data.es.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * elasticsearch基础配置
 */
@Data
@ConfigurationProperties(prefix = "sit.elasticsearch")
public class ElasticSearchProperties {

    private String clusterName = "elasticsearch";

    private String clusterNodes = "127.0.0.1:9300";

    private String userName = "elastic";

    private String password = "changeme";

    private String schema = "http"; // 使用的协议

    private int connectTimeOut = 1000; // 连接超时时间

    private int socketTimeOut = 30000; // 连接超时时间

    private int connectionRequestTimeOut = 500; // 获取连接的超时时间

    private int maxConnectNum = 30; // 最大连接数

    private int maxConnectPerRoute = 10; // 最大路由连接数

}
