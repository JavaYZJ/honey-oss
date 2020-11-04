package com.eboy.honeyoss.demo;

import com.eboy.honey.oss.api.HoneyOss;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yangzhijie
 * @date 2020/11/4 11:30
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class SdkTest {

    @Autowired
    HoneyOss honeyOss;

    @Test
    public void test() {
        String url = honeyOss.downAsUrl("honey-oss-dev", "52c1db014d80bd4b4dbf7fe1bc37a3c5");
        log.info(url);
    }
}
