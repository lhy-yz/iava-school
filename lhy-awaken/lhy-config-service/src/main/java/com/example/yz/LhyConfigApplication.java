package com.example.yz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @author lhy
 * @date 2024/9/7
 * @apiNote
 **/
@SpringBootApplication
@Slf4j
public class LhyConfigApplication {
    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext bootContext= SpringApplication.run(LhyConfigApplication.class, args);
        while (true){
            String username = bootContext.getEnvironment().getProperty("username");
            String age = bootContext.getEnvironment().getProperty("age");
            String appName = bootContext.getEnvironment().getProperty("spring.application.name");
            log.info("username："+username);
            log.info("age："+age);
            log.info("appName："+appName);
            TimeUnit.SECONDS.sleep(1L);
        }

    }
}
