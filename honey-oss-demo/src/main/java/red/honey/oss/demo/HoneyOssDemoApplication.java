package red.honey.oss.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import red.honey.oss.annotation.EnableHoneyOss;

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
