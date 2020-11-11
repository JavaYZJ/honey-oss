package com.eboy.honey.oss.server.file;

import com.eboy.honey.oss.api.utils.HoneyFileUtil;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.service.ThumbnailService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
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
    @Autowired
    private FileService fileService;


    @SneakyThrows
    @Test
    public void thumbnail() {
        File file = new File("F:\\yangzhijie520.jpeg");
        FileInputStream inputStream = new FileInputStream(file);
        Thumbnails.of(file).size(520, 520).toFile("F:\\yangzhijie_thumbnail.jpeg");
        File file1 = new File("F:\\yangzhijie_thumbnail.jpeg");
        fileService.upload(file1, bucketName, MediaType.IMAGE_JPEG);
    }

    @SneakyThrows
    @Test
    public void thumbnail1() {
        File file = new File("F:\\yangzhijie520.jpeg");
        Thumbnails.of(file).scale(0.5f).toFile("F:\\yangzhijie_thumbnail5.jpeg");
    }

    @Test
    @SneakyThrows
    public void thumbnail2() {
        File file = new File("F:\\yangzhijie520.jpeg");
        Thumbnails.of(file).scale(1.0f).watermark(Positions.CENTER, ImageIO.read(new File("F:\\test.jpg")), 1.0f).toFile("F:\\test2.jpg");

    }


    @Test
    public void getUrlByOriginalPicture() {
        String url = thumbnailService.getUrlByOriginalPicture(bucketName, "41d16383020f27c1e43a6b75b3b30d14");
        log.info(url);
    }

    @Test
    public void getUrlByOriginalPicture1() {
        String url = thumbnailService.getUrlByOriginalPicture(bucketName, "b7572e9f8bdc3d7968af1445055e287e", 60);
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
