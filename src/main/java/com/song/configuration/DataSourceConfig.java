package com.song.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by 17060342 on 2019/8/27.
 */
@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.primary.url}")
    private String primaryurl;

    @Value("${spring.datasource.primary.username}")
    private String primaryusername;

    @Value("${spring.datasource.primary.password}")
    private String primarypassword;

    @Value("${spring.datasource.primary.driver-class-name}")
    private String primarydriverClassName;

    @Value("${spring.datasource.secondary.url}")
    private String secondaryurl;

    @Value("${spring.datasource.secondary.username}")
    private String secondaryusername;

    @Value("${spring.datasource.secondary.password}")
    private String secondarypassword;

    @Value("${spring.datasource.secondary.driver-class-name}")
    private String secondarydriverClassName;

    @Value("${spring.druid.initialSize}")
    private int initialSize;

    @Value("${spring.druid.minIdle}")
    private int minIdle;

    @Value("${spring.druid.maxActive}")
    private int maxActive;

    @Value("${spring.druid.maxWait}")
    private int maxWait;

    @Value("${spring.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.druid.validationQuery}")
    private String validationQuery;

    @Value("${spring.druid.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.druid.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.druid.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.druid.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.druid.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.druid.filters}")
    private String filters;

    @Value("{spring.druid.connectionProperties}")
    private String connectionProperties;

    @Bean(name = "secondaryDataSource")
    @Qualifier(value = "secondaryDataSource")  //spring装配bean的唯一标识
    //@ConfigurationProperties(prefix = "spring.datasource.secondary")   //application.properties配置文件中该数据源的配置前缀
    public DataSource secondaryDataSource(){

        //return DataSourceBuilder.create().build();

        DruidDataSource secondarydatasource = new DruidDataSource();
        secondarydatasource.setUrl(secondaryurl);
        secondarydatasource.setUsername(secondaryusername);
        secondarydatasource.setPassword(secondarypassword);   //这里可以做加密处理
        secondarydatasource.setDriverClassName(secondarydriverClassName);

        //configuration
        setDruidDataSource(secondarydatasource);
        //关闭失败重连，该数据库可能不存在
        secondarydatasource.setConnectionErrorRetryAttempts(0);
        secondarydatasource.setBreakAfterAcquireFailure(true);
        return secondarydatasource;
    }

    @Primary    //配置该数据源为主数据源
    @Bean(name = "primaryDataSource")
    @Qualifier(value = "primaryDataSource")
    //@ConfigurationProperties(prefix = "spring.datasource.primary")   //application.properties配置文件中该数据源的配置前缀
    public DataSource primaryDataSource(){
        //return DataSourceBuilder.create().build();

        DruidDataSource primarydatasource = new DruidDataSource();
        primarydatasource.setDriverClassName(primarydriverClassName);
        primarydatasource.setUrl(primaryurl);
        primarydatasource.setUsername(primaryusername);
        primarydatasource.setPassword(primarypassword);   //这里可以做加密处理

        //configuration
        setDruidDataSource(primarydatasource);

        return primarydatasource;
    }

    private void setDruidDataSource(DruidDataSource datasource){
        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {

        }
        datasource.setConnectionProperties(connectionProperties);
    }
}
