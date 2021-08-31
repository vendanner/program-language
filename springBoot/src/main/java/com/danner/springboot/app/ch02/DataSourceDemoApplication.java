//package com.danner.springboot.app.ch02;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//
//
///**
// * 打印 data source 连接信息
// */
//@SpringBootApplication
//@Slf4j
//public class DataSourceDemoApplication implements CommandLineRunner {
//
//    @Autowired
//    private DataSource dataSource;
//
//    public static void main(String[] args) {
//        SpringApplication.run(DataSourceDemoApplication.class, args);
//    }
//
//    public void run(String... args) throws Exception{
//        showConnection();
//    }
//
//    public void showConnection() throws Exception {
//        log.info("datasource ==> " + dataSource.toString());
//        Connection connection = dataSource.getConnection();
//        // 默认是内置 h2数据库：HikariProxyConnection@690287064 wrapping conn0:
//        //      url=jdbc:h2:mem:994604f5-979d-4e09-8c92-980e3763648e user=SA
//        log.info("connection ==> " + connection.toString());
//        connection.close();
//    }
//}
