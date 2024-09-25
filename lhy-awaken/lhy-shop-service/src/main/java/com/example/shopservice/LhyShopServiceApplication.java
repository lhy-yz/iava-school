package com.example.shopservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lhy
 * @date 2024/9/6
 * @apiNote
 **/
@SpringBootApplication
public class LhyShopServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LhyShopServiceApplication.class, args);
    }

}
