package com.eboy.honeyoss.demo;


import com.eboy.honey.oss.api.service.FileRpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yangzhijie
 * @date 2020/11/3 14:11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class CommonTest {

    @Reference(version = "1.0")
    private FileRpcService fileRpcService;

    final String bucketName = "honey-oss-dev";

    @Test
    public void test() {
        String url = fileRpcService.downAsUrl(bucketName, "52c1db014d80bd4b4dbf7fe1bc37a3c5");
        log.info(url);
    }
}
