package com.song.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by feng on 2019/5/26.
 * mvn clean package -DskipTests
 */
@SpringBootApplication
@ServletComponentScan(basePackages = "com.song.config")
@ComponentScan(basePackages = "com.song")
@PropertySource("classpath:mail.properties")
@EnableTransactionManagement
public class Entry {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Entry.class, args);
    }
}
