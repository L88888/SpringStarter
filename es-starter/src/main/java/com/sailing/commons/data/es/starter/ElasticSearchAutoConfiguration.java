package com.sailing.commons.data.es.starter;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @program: spring-starter
 * @description: ElasticSearchAutoConfiguration 初始化配置elasticsearch客户端实例
 * @author: LIULEI
 * @create: 2020-06-27 17:35:RocketMqConfiguration
 */
@Slf4j
@EnableConfigurationProperties(ElasticSearchProperties.class)
public class ElasticSearchAutoConfiguration implements DisposableBean{

    /**
     * 客户端实例定义
     */
    private SailingRestHighLevelClient sailingRestHighLevelClient;

    @Resource
    private ElasticSearchProperties properties;

    /**
     * 连接elasticsearch客户端方法封装
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(TransportClient.class)
    public SailingRestHighLevelClient transportClient() {
        log.info("=======" + properties.getClusterName());
        log.info("=======" + properties.getClusterNodes());

        ArrayList<HttpHost> hostList = new ArrayList<>();
        String[] hostStrs = properties.getClusterNodes().split(",");
        Object[] tempData = null;
        for (String hostAdd : hostStrs) {
            tempData = hostAdd.split(":");
            hostList.add(new HttpHost(String.valueOf(tempData[0]),
                    Integer.parseInt(String.valueOf(tempData[1])), properties.getSchema()));
        }
        RestClientBuilder builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
        // 异步httpclient连接延时配置
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                requestConfigBuilder.setConnectTimeout(properties.getConnectTimeOut());
                requestConfigBuilder.setSocketTimeout(properties.getSocketTimeOut());
                requestConfigBuilder.setConnectionRequestTimeout(properties.getConnectionRequestTimeOut());
                return requestConfigBuilder;
            }
        });
        // 异步httpclient连接数配置
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                httpClientBuilder.setMaxConnTotal(properties.getMaxConnectNum());
                httpClientBuilder.setMaxConnPerRoute(properties.getMaxConnectPerRoute());
                return httpClientBuilder;
            }
        });
        sailingRestHighLevelClient = new SailingRestHighLevelClient(builder);
        return sailingRestHighLevelClient;
    }

    /**
     * 设置xpack加密密码
     * @return
     */
    private Settings settings() {
        return Settings.builder()
                .put("cluster.name", properties.getClusterName())
                .put("xpack.security.user", properties.getUserName() +
                        ":" + properties.getPassword())
                .build();
    }

    @Override
    public void destroy() throws Exception {
        log.info("开始销毁Es的连接");
        if (sailingRestHighLevelClient != null) {
            sailingRestHighLevelClient.close();
        }
    }
}
