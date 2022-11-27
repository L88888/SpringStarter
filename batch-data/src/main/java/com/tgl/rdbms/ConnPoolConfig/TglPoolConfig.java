package com.tgl.rdbms.ConnPoolConfig;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author Comsys-LIULEI
 * @version V1.0
 * @Title: DruidConfig 读取数据源配置信息并初始化DataSource对象
 * @Package DIPFB
 * @Description: DruidConfig
 * Copyright: Copyright (c) 2011
 * Company:上海熙菱信息技术有限公司
 * @date 2018/4/28 12:05:51下午
 */
@Configuration
@ConfigurationProperties(
        prefix = "spring.datasource.primary"
)
@Slf4j
@Data
public class TglPoolConfig {

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    private int initialSize;

    private int minIdle;

    private int maxActive;

    private int maxWait;

    private int timeBetweenEvictionRunsMillis;

    private int minEvictableIdleTimeMillis;

    private String validationQuery;

    private boolean testWhileIdle;

    private boolean testOnBorrow;

    private boolean testOnReturn;

    private boolean poolPreparedStatements;

    private int maxPoolPreparedStatementPerConnectionSize;

    private String filters;

    private String connectionProperties;

    private HikariDataSource hikariDataSource = null;

//    <bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource" destroy-method="close">
//        <property name="driverClassName" value="${db.driver}"/>
//        <property name="jdbcUrl" value="${db.url}"/>
//        <property name="username" value="${db.username}"/>
//        <property name="password" value="${db.password}"/>
//        <!-- 配置连接池最小值、最大值 -->
//        <property name="minimumIdle" value="5" />
//        <property name="maximumPoolSize" value="15" />
//        <!-- 配置获取连接等待超时的时间 -->
//        <property name="connectionTimeout" value="60000" />
//        <property name="connectionTestQuery" value="SELECT 1"/>
//        <property name="validationTimeout" value="5000"/>
//        <property name="poolName" value="CcosDataSource"/>
//        <property name="idleTimeout" value="60000" />
//        <property name="maxLifetime" value="600000" />
//    </bean>

    @Bean
    @Primary
    public HikariDataSource dataSource() {
        log.info("数据库连接池创建中.......");
        if (hikariDataSource != null){
            return hikariDataSource;
        }

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(this.url);
        hikariConfig.setUsername(this.username);
        hikariConfig.setPassword(this.password);
        hikariConfig.setDriverClassName(this.driverClassName);
        hikariConfig.setConnectionTimeout(60000);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setIdleTimeout(60000);
        hikariConfig.setMaxLifetime(600000);
        hikariConfig.setPoolName("TglDataSource");
        hikariConfig.setValidationTimeout(5000);
        hikariConfig.setMaximumPoolSize(15);
        hikariConfig.setMinimumIdle(5);
        hikariConfig.setAutoCommit(false);
        hikariDataSource = new HikariDataSource(hikariConfig);
        log.info("数据库连接池创建完成.......");
        return hikariDataSource;
    }
}