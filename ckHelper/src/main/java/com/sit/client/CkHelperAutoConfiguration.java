package com.sit.client;

import com.sit.client.ckhelper.service.ClickHouseData;
import com.sit.client.config.ClickHouseProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gdyang
 * @date 2022/5/18 2:52 下午
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(CkHelperAutoConfiguration.class)
public class CkHelperAutoConfiguration implements DisposableBean {

    @Bean
    public ClickHouseProperties clickHouseProperties(){
        return new ClickHouseProperties();
    }

    @Bean
    public ClickHouseData clickHouseData(ClickHouseProperties properties){
        return new ClickHouseData(properties);
    }

    @Override
    public void destroy() throws Exception {

    }
}