package com.eboy.honey.oss.server.file;

import com.eboy.honey.oss.server.application.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

/**
 * @author yangzhijie
 * @date 2020/8/14 11:38
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class FileTest {

    @Value("${honey.oss.minio.bucketName}")
    private String bucketName;

    @Autowired
    private FileService fileService;


    @Test
    public void fileUpload() {
        String pathName = "E:\\sentinel-dashboard-1.7.1.jar";
        File file = new File(pathName);
        boolean upload = fileService.upload(file, bucketName, ContentType.APPLICATION_OCTET_STREAM);
        log.info("是否上传成功：{}", upload);
    }


}
