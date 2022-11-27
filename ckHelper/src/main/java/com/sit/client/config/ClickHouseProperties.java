package com.sit.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gdyang
 * @date 2022/5/18 5:21 下午
 */
@Data
@ConfigurationProperties(prefix = "business.ck")
public class ClickHouseProperties {

    private String endpoint;

    private String username;

    private String password;
}
