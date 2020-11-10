package com.eboy.honey.oss.demo;

import com.eboy.honey.oss.api.HoneyOss;
import com.eboy.honey.oss.api.entiy.CallBack;
import com.eboy.honey.oss.client.HoneyMiniO;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

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
    @Autowired
    MinioClient minioClient;
    @Autowired
    private HoneyMiniO honeyMiniO;
    @Value("${honey.oss.bucketName}")
    private String bucketName;

    @Autowired
    private TestApi testApi;

    @Test
    public void test() {
        String url = honeyOss.downAsUrl("honey-oss-dev", "f723b04005c11ab3710618f247611883");
        log.info(url);
    }

    @Test
    public void upload() {
        String pathName = "C:\\Users\\admin\\Pictures\\Saved Pictures\\2.jpg";
        File file = new File(pathName);
        String upload = honeyOss.upload(file, bucketName, MediaType.IMAGE_JPEG);
        log.debug(upload);
    }

    @Test
    public void test1() {
        String pathName = "C:\\Users\\admin\\Pictures\\Saved Pictures\\2.jpg";
        File file = new File(pathName);
        String upload = testApi.upload(file, bucketName, MediaType.IMAGE_JPEG);
        log.debug(upload);
    }

    @Test
    public void asyncUpload() {
        String pathName = "C:\\Users\\admin\\Pictures\\Saved Pictures\\2.jpg";
        File file = new File(pathName);
        CallBack callBack = new CallBack();
        callBack.setCallBackHttpUrl("http://localhost:8089/rest/callback");
        testApi.asyncUpload(file, bucketName, MediaType.IMAGE_JPEG, callBack);
    }
}
