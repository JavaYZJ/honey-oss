package com.eboy.honey.oss.server.file;

import com.eboy.honey.oss.server.application.service.ThumbnailService;
import com.eboy.honey.oss.utils.HoneyFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;

/**
 * @author yangzhijie
 * @date 2020/8/26 16:34
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ThumbnailTest {

    @Value("${honey.oss.minio.bucketName}")
    private String bucketName;

    @Autowired
    private ThumbnailService thumbnailService;

    @Test
    public void getUrlByOriginalPicture() {
        String url = thumbnailService.getUrlByOriginalPicture(bucketName, "b7572e9f8bdc3d7968af1445055e287e");
        log.info(url);
    }


    @Test
    public void down2LocalByOriginalPicture() {
        thumbnailService.down2LocalByOriginalPicture(bucketName, "6de44f6131bc1eb58ec24cdf07c4d800", "F:\\thumbnail.jpeg");
    }

    @Test
    public void getStreamByOriginalPicture() {
        InputStream stream = thumbnailService.getStreamByOriginalPicture(bucketName, "6de44f6131bc1eb58ec24cdf07c4d800");
        HoneyFileUtil.writeToLocal("F:\\writexxxx.jpg", stream);
    }
}
