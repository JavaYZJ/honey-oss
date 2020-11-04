package com.eboy.honeyoss.demo;

import com.eboy.honey.oss.annotation.EnableHoneyOss;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableHoneyOss
public class HoneyOssDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoneyOssDemoApplication.class, args);
    }

}
