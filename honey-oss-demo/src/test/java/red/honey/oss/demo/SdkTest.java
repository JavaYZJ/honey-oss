package red.honey.oss.demo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.minio.ComposeObjectArgs;
import io.minio.ComposeSource;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.geometry.Positions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import red.honey.oss.api.HoneyOss;
import red.honey.oss.api.constant.ImageType;
import red.honey.oss.api.dto.FileDto;
import red.honey.oss.api.dto.FileShardDto;
import red.honey.oss.api.entiy.CallBack;
import red.honey.oss.api.entiy.Response;
import red.honey.oss.api.entiy.Thumbnail;
import red.honey.oss.api.entiy.WaterMark;
import red.honey.oss.client.HoneyMiniO;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
        String url = honeyOss.downAsUrl("honey-oss-dev", "6357c75ec726f4b0eaeea45a6e6269b1");
        log.info(url);
    }

    @Test
    public void upload() {
        String pathName = "E:\\学习\\Java进阶\\05.dubbo快速学习 6课\\[www.javaxxz.com]01（传统架构图）.wmv";
        File file = new File(pathName);
        String upload = honeyOss.upload(file, bucketName, MediaType.APPLICATION_OCTET_STREAM).getFileKey();
        log.debug(upload);
    }


    @Test
    public void uploadByShard() throws IOException, InterruptedException {
        String pathName = "C:\\Users\\admin\\Downloads\\Downloads.zip";
        Response<FileShardDto> response = honeyOss.uploadByShard(pathName, bucketName, MediaType.APPLICATION_OCTET_STREAM);
        log.debug(JSON.toJSONString(response));
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

    @Test
    public void thumbnail() {
        String pathName = "C:\\Users\\admin\\Pictures\\Saved Pictures\\8.jpg";
        File file = new File(pathName);
        String image = honeyOss.uploadImage(file, bucketName, MediaType.IMAGE_JPEG, true);
        log.info(image);
    }

    @Test
    public void thumbnail1() {
        String thumbnail = honeyOss.getUrlByOriginalPicture(bucketName, "6357c75ec726f4b0eaeea45a6e6269b1");
        log.info(thumbnail);
    }

    @Test
    public void thumbnail2() {
        String pathName = "C:\\Users\\admin\\Pictures\\Saved Pictures\\10.jpg";
        File file = new File(pathName);
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setInputSource(file);
        thumbnail.setRotate(90d);
        thumbnail.setOutputFormat(ImageType.PNG);
        WaterMark waterMark = new WaterMark();
        waterMark.setPositions(Positions.BOTTOM_LEFT);
        waterMark.setTransparency(0.5f);
        waterMark.setWaterMarkSource(new File("C:\\Users\\admin\\Pictures\\Saved Pictures\\9.jpg"));
        thumbnail.setWaterMark(waterMark);
        String image = honeyOss.uploadImage(file, bucketName, MediaType.IMAGE_JPEG, thumbnail);
        log.info(image);
    }


    @Test
    public void testFileInfo() {
        List<FileDto> fileByFileKeys = honeyOss.getFileByFileKeys(Lists.newArrayList("6357c75ec726f4b0eaeea45a6e6269b1"));
        log.info(JSON.toJSONString(fileByFileKeys));
    }


    @Test
    public void compose() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        List<ComposeSource> sourceObjectList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sourceObjectList.add(
                    ComposeSource.builder().bucket(bucketName)
                            .object("2G.zip_" + i + "(ec856292031563af2a83b67d5ba0877c).temp")
                            .build()
            );
        }
        minioClient.composeObject(
                ComposeObjectArgs.builder()
                        .bucket(bucketName)
                        .object("2G.zip_compose.exe")
                        .sources(sourceObjectList)
                        .build());
    }
}
