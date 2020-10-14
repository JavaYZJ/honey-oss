package com.eboy.honey.oss.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * honey-oss master server
 *
 * @author yangzhijie
 */
@SpringBootApplication(exclude = {MultipartAutoConfiguration.class})
@EnableAsync
public class HoneyOssServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoneyOssServerApplication.class, args);
    }

}
