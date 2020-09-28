package com.eboy.honey.oss.file;

import com.eboy.honey.oss.dto.HoneyStream;
import com.eboy.honey.oss.dubbo.FileRpcService;
import com.eboy.honey.oss.utils.HoneyFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;

/**
 * @author yangzhijie
 * @date 2020/8/28 16:48
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class FileTest {

    @Reference(version = "1.0")
    private FileRpcService fileRpcService;

    final String bucketName = "honey-oss-dev";

    @Test
    public void test() {
        String url = fileRpcService.downAsUrl(bucketName, "6de44f6131bc1eb58ec24cdf07c4d800");
        log.info(url);
    }

    @Test
    public void test1() {
        HoneyStream honeyStream = fileRpcService.downAsStream(bucketName, "6de44f6131bc1eb58ec24cdf07c4d800");
        InputStream inputStream = honeyStream.getInputStream();
        HoneyFileUtil.writeToLocal("F:\\dubbo.jpeg", inputStream);
    }

}
