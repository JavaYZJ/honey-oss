package com.eboy.honey.oss.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author yangzhijie
 */
@SpringBootApplication
@EnableAsync
public class HoneyOssServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoneyOssServerApplication.class, args);
    }

}
