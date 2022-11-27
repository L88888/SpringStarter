package com.sit.client.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

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
@Component
@ConfigurationProperties(
        prefix = "spring.datasource"
)
@Slf4j
@Data
public class DruidConfig {

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

    /**
     * 在同样的DataSource中，首先使用被标注的DataSource(Primary)
     * @return
     */
//    @Bean
//    @Primary
//    public DataSource dataSource() {
//        // 配置数据库连接基础信息
//        DruidDataSource datasource = new DruidDataSource();
//        datasource.setUrl(this.url);
//        datasource.setUsername(username);
//        datasource.setPassword(password);
//        datasource.setDriverClassName(driverClassName);
//
//        // 设置连接池基本信息
//        datasource.setInitialSize(initialSize);
//        datasource.setMinIdle(minIdle);
//        datasource.setMaxActive(maxActive);
//        datasource.setMaxWait(maxWait);
//        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
//        datasource.setValidationQuery(validationQuery);
//        datasource.setTestWhileIdle(testWhileIdle);
//        datasource.setTestOnBorrow(testOnBorrow);
//        datasource.setTestOnReturn(testOnReturn);
//        datasource.setPoolPreparedStatements(poolPreparedStatements);
//        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
//        try {
//            datasource.setFilters(filters);
//        } catch (SQLException e) {
//            log.debug("druid configuration initialization filter : {0}", e);
//        }
//        datasource.setConnectionProperties(connectionProperties);
//        List<Filter> filterList = new ArrayList<>();
//        filterList.add(wallFilter());
//        datasource.setProxyFilters(filterList);
//        return datasource;
//    }

    @Bean
    @Primary
    public HikariDataSource dataSource() {
        log.info("数据库连接池<Hikari>创建中.......");
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
        hikariConfig.setMaxLifetime(60000);
        hikariConfig.setPoolName("TglDataSource");
        hikariConfig.setValidationTimeout(5000);
        hikariConfig.setMaximumPoolSize(15);
        hikariConfig.setMinimumIdle(5);
        hikariConfig.setAutoCommit(false);
        hikariDataSource = new HikariDataSource(hikariConfig);
        log.info("数据库连接池<Hikari>创建完成.......");
        return hikariDataSource;
    }

    /**
     * 注册一个StatViewServlet
     * @return
     */
    @Bean
    public ServletRegistrationBean DruidStatViewServle2(){
        ServletRegistrationBean servletRegistrationBean =
                new ServletRegistrationBean(new StatViewServlet(),"/druid2/*");

        //添加初始化参数：initParams 部署后可以放开
        /** 白名单，如果不配置或value为空，则允许所有 */
        servletRegistrationBean.addInitParameter("allow","127.0.0.1");
        /** 黑名单，与白名单存在相同IP时，优先于白名单 */
        servletRegistrationBean.addInitParameter("deny","192.0.0.1");
        /** 用户名 */
        servletRegistrationBean.addInitParameter("loginUsername","admin");
        /** 密码 */
        servletRegistrationBean.addInitParameter("loginPassword","sailingadmin123");
        /** 禁用页面上的“Reset All”功能 */
        servletRegistrationBean.addInitParameter("resetEnable","false");
        return servletRegistrationBean;
    }

    /**
     * 注册一个：WebStatFilter
     * @return
     */
    @Bean
    public FilterRegistrationBean druidStatFilter2(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());

        /** 过滤规则 */
        filterRegistrationBean.addUrlPatterns("/*");
        /** 忽略资源 */
        filterRegistrationBean.addInitParameter("exclusions",
                "*.html,*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid2/*");
        return filterRegistrationBean;
    }

    @Bean
    public WallFilter wallFilter() {
        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig());
        return wallFilter;
    }

    @Bean
    public WallConfig wallConfig() {
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);//允许一次执行多条语句
        config.setNoneBaseStatementAllow(true);//允许非基本语句的其他语句
        return config;
    }

}
