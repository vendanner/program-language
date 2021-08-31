package com.danner.springboot.app.ch02;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 多数据源测试，配置文件中配置多数据源
 *
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class})
@Slf4j
public class MultiDataSourceDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MultiDataSourceDemoApplication.class, args);
    }

    /**
     * foo.datasource 数据源，配置文件中已配置
     * @return
     */
    @Bean
    @ConfigurationProperties("foo.datasource")
    public DataSourceProperties fooDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 创建 foo 数据源
     * @return
     */
    @Bean
    public DataSource fooDataSource() {
        DataSourceProperties dataSourceProperties = fooDataSourceProperties();
        log.info("foo datasource: {}", dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    /**
     * 创建 foo 数据源 事务
     * @param fooDataSource
     * @return
     */
    @Bean
    @Resource
    public PlatformTransactionManager fooTxManager(DataSource fooDataSource) {
        return new DataSourceTransactionManager(fooDataSource);
    }


    /**
     * bar 数据源相关操作
     * @return
     */
    @Bean
    @ConfigurationProperties("bar.datasource")
    public DataSourceProperties barDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Bean 是指有 spring 自动创建的组件，DataSource 就是当成 Bean 返回
     * 此 Bean 的名称就是函数名 barDataSource
     * Bean 作用到哪里，生成的 Bean 名称就是它本身(函数名，类名，属性名)
     * @return
     */
    @Bean
    public DataSource barDataSource() {
        DataSourceProperties dataSourceProperties = barDataSourceProperties();
        log.info("bar datasource: {}", dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    /**
     * Resource 是说我这个方法的参数要按照名字来注入其他的Bean，本例中需要的 barDataSource
     * @param barDataSource
     * @return
     */
    @Bean
    @Resource
    public PlatformTransactionManager barTxManager(DataSource barDataSource) {
        return new DataSourceTransactionManager(barDataSource);
    }
}
