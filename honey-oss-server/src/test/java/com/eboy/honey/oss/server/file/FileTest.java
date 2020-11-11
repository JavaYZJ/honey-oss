package com.eboy.honey.oss.server.file;

import cn.xsshome.taip.vision.TAipVision;
import com.alibaba.fastjson.JSON;
import com.eboy.honey.oss.api.constant.ImageType;
import com.eboy.honey.oss.api.entiy.Thumbnail;
import com.eboy.honey.oss.api.entiy.WaterMark;
import com.eboy.honey.oss.api.utils.HoneyFileUtil;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.service.ThumbnailService;
import com.eboy.honey.oss.server.application.vo.FileVo;
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

    @Value("${honey.oss.appId}")
    private String appId;
    @Value("${honey.oss.appKey}")
    private String appKey;

    @Autowired
    private FileService fileService;

    @Autowired
    private ThumbnailService thumbnailService;


    @Test
    public void fileUpload() {
        String pathName = "C:\\Users\\admin\\Pictures\\Saved Pictures\\2.jpg";
        File file = new File(pathName);
        String upload = fileService.upload(file, "public1", MediaType.IMAGE_JPEG);
        log.info("是否上传成功：{}", !upload.isEmpty());
    }

    @Test
    public void fileUploadAsync() {
        String pathName = "C:\\Users\\admin\\Pictures\\Saved Pictures\\2.jpg";
        File file = new File(pathName);
        fileService.asyncUpload(file, bucketName, MediaType.IMAGE_JPEG, "http://localhost:8089/rest/callback");
    }

    @Test
    public void downAsUrl() {
        String url = fileService.downAsUrl(bucketName, "52c1db014d80bd4b4dbf7fe1bc37a3c5", 60);
        log.info(url);
    }

    @Test
    public void downAsUrl1() {
        String url = fileService.downAsUrl(bucketName, "52c1db014d80bd4b4dbf7fe1bc37a3c5");
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
        List<FileVo> file = fileService.getFileByFileKeys(Lists.newArrayList("6357c75ec726f4b0eaeea45a6e6269b1"));
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
        File file = new File("C:\\Users\\admin\\Pictures\\Saved Pictures\\test.jpg");
        String fileKey = fileService.uploadImage(file, bucketName, MediaType.IMAGE_JPEG, true);
        log.info(fileKey);
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

    @Test
    public void pornTest() throws Exception {
        TAipVision aipVision = new TAipVision(appId, appKey);
        String rs = aipVision.visionPornByURL("https://image.baidu.com/search/detail?ct=503316480&z=undefined&tn=baiduimagedetail&ipn=d&word=huangse&step_word=&ie=utf-8&in=&cl=undefined&lm=undefined&st=undefined&hd=undefined&latest=undefined&copyright=undefined&cs=1953689970,1496824567&os=3074260117,539067031&simid=3365255763,111285436&pn=4&rn=1&di=113740&ln=1786&fr=&fmq=1604383086349_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&is=0,0&istype=0&ist=&jit=&bdtype=0&spn=0&pi=0&gsm=0&objurl=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F17%2F02%2F04%2F4143b4c2a84874d841952bc29e911ada.jpg%2521%2Ffwfh%2F804x723%2Fquality%2F90%2Funsharp%2Ftrue%2Fcompress%2Ftrue&rpstart=0&rpnum=0&adpicid=0&force=undefined&ctd=1604383091856^3_1903X903%1");
        log.info(JSON.toJSONString(rs));
    }

}
