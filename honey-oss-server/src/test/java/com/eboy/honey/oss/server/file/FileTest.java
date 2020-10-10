package com.eboy.honey.oss.server.file;

import com.alibaba.fastjson.JSON;
import com.eboy.honey.oss.constant.ImageType;
import com.eboy.honey.oss.entiy.Thumbnail;
import com.eboy.honey.oss.entiy.WaterMark;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.service.ThumbnailService;
import com.eboy.honey.oss.server.application.vo.FileVo;
import com.eboy.honey.oss.utils.HoneyFileUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.geometry.Positions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/8/14 11:38
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class FileTest {

    private final String FILE_KEY = "b7572e9f8bdc3d7968af1445055e287e";

    @Value("${honey.oss.minio.bucketName}")
    private String bucketName;

    @Autowired
    private FileService fileService;

    @Autowired
    private ThumbnailService thumbnailService;


    @Test
    public void fileUpload() {
        String pathName = "C:\\Users\\admin\\Pictures\\Saved Pictures\\2.jpg";
        File file = new File(pathName);
        String upload = fileService.upload(file, "public", MediaType.IMAGE_JPEG);
        log.info("是否上传成功：{}", !upload.isEmpty());
    }

    @Test
    public void fileUploadAsync() {
        String pathName = "C:\\Users\\admin\\Pictures\\Saved Pictures\\6.jpg";
        File file = new File(pathName);
        fileService.asyncUpload(file, bucketName, MediaType.IMAGE_JPEG, "http://localhost:8089/rest/callback");
    }

    @Test
    public void downAsUrl() {
        String url = fileService.downAsUrl(bucketName, "3788e6c1e7baa60d62492ba72149499f", 60);
        log.info(url);
    }

    @Test
    public void downAsUrl1() {
        String url = fileService.downAsUrl(bucketName, "b7572e9f8bdc3d7968af1445055e287e");
        log.info(url);
    }

    @Test
    public void downAsStream() {
        InputStream inputStream = fileService.downAsStream(bucketName, "b7572e9f8bdc3d7968af1445055e287e");
        HoneyFileUtil.writeToLocal("F:\\write.jpg", inputStream);

    }

    @Test
    public void getFileByIds() {
        List<FileVo> file = fileService.getFileByIds(Lists.newArrayList("ba34a242338047a3b2ce4464cf37a907", "e95559fa2c2646909ad89f1b17afcc5f", "a5edc1d0a02342e98cfe2b80f5ebb702"));
        log.info(JSON.toJSONString(file));
    }

    @Test
    public void getFileByFileKeys() {
        List<FileVo> file = fileService.getFileByFileKeys(Lists.newArrayList("6de44f6131bc1eb58ec24cdf07c4d800", "fd83d2d860d3a3cfdcd367ce3deb9f5a", "b7572e9f8bdc3d7968af1445055e287e"));
        log.info(JSON.toJSONString(file));
    }

    @Test
    public void down2local() {
        fileService.down2Local(bucketName, FILE_KEY, "F:\\local.jpg");
    }

    @Test
    public void objectName() {
        String objectNameByFileKey = HoneyFileUtil.buildObjectNameByFileKey("yangzhijie9999.jpeg", "123456");
        log.info(objectNameByFileKey);
    }


    @Test
    public void test() {
        File file = new File("C:\\Users\\admin\\Pictures\\Saved Pictures\\5.jpg");
        String fileKey = fileService.uploadImage(file, bucketName, MediaType.IMAGE_JPEG, true);
        String url = fileService.downAsUrl(bucketName, fileKey, 60);
        log.info(url);
    }

    @Test
    public void test1() {
        File file = new File("C:\\Users\\admin\\Pictures\\Saved Pictures\\2.jpg");
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setInputSource(file);
        thumbnail.setRotate(90d);
        thumbnail.setOutputFormat(ImageType.PNG);
        WaterMark waterMark = new WaterMark();
        waterMark.setPositions(Positions.BOTTOM_LEFT);
        waterMark.setTransparency(0.5f);
        waterMark.setWaterMarkSource(new File("C:\\Users\\admin\\Pictures\\Saved Pictures\\5.jpg"));
        thumbnail.setWaterMark(waterMark);
        String fileKey = fileService.uploadImage(file, bucketName, MediaType.IMAGE_JPEG, thumbnail);
        String url = thumbnailService.getUrlByOriginalPicture(bucketName, fileKey, 60);
        log.info(url);
    }


}
