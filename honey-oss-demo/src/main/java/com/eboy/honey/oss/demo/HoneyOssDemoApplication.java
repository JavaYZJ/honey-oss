package com.eboy.honey.oss.demo;

import com.eboy.honey.oss.annotation.EnableHoneyOss;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yangzhijie
 */
@SpringBootApplication
@EnableHoneyOss
public class HoneyOssDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoneyOssDemoApplication.class, args);
    }

}
