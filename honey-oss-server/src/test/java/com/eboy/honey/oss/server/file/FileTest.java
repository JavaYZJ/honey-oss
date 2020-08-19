package com.eboy.honey.oss.server.file;

import com.eboy.honey.oss.dto.HoneyStream;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.utils.BeanConvertUtil;
import com.eboy.honey.oss.server.application.vo.FileVo;
import com.eboy.honey.oss.server.client.HoneyMiniO;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.xmlpull.v1.XmlPullParserException;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

    @Autowired
    private HoneyMiniO honeyMiniO;


    @Test
    public void fileUpload() {
        String pathName = "E:\\学习\\Java进阶\\02.dubbo分布式架构基础篇 19课\\[www.javaxxz.com]第00节--课程介绍.avi";
        File file = new File(pathName);
        boolean upload = fileService.upload(file, bucketName, MediaType.APPLICATION_OCTET_STREAM);
        log.info("是否上传成功：{}", upload);
    }

    @Test
    public void down() {
        String url = fileService.downAsUrl(bucketName, "[www.javaxxz.com]第00节--课程介绍.avi");
        log.info(url);
    }

    @SneakyThrows
    @Test
    public void down1() {
        FileOutputStream fos = new FileOutputStream("F:\\test.avi");
        InputStream is = fileService.downAsStream(bucketName, "[www.javaxxz.com]第00节--课程介绍.avi");
        byte[] b = new byte[1024];
        int length;
        while ((length = is.read(b)) > 0) {
            fos.write(b, 0, length);
        }
        is.close();
        fos.close();
        log.info("");
    }

    @Test
    public void upload() throws NoSuchAlgorithmException, IOException, InvalidKeyException, XmlPullParserException {
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient("http://49.235.208.98:9000", "YANGZHIJIEHONEYOSS", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket("asiatrip");
            }

            // 使用putObject上传一个文件到存储桶中。
            minioClient.putObject(bucketName, "yangzhijie.jpeg", "E:\\yangzhijie.jpeg");
            System.out.println("yangzhijie.jpeg is successfully uploaded as yangzhijie.jpeg to `asiatrip` bucket.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    @Test
    public void test() throws NoSuchAlgorithmException, IOException, InvalidKeyException, XmlPullParserException {
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient("http://49.235.208.98:9000", "YANGZHIJIEHONEYOSS", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket(bucketName);
            }
            String pathName = "E:\\yangzhijie12.jpeg";
            File file = new File(pathName);
            FileInputStream fileInputStream = new FileInputStream(file);
            FileVo fileVo = new FileVo();
            HoneyStream honeyStream = new HoneyStream(fileInputStream);
            fileVo.setHoneyStream(honeyStream);
            InputStream inputStream = fileVo.getHoneyStream().getInputStream();
            // 使用putObject上传一个文件到存储桶中。
            minioClient.putObject(bucketName, "yangzhijie666.jpeg", inputStream, MediaType.IMAGE_JPEG.toString());
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }

    }

    @SneakyThrows
    @Test
    public void test1() {
        String pathName = "E:\\yangzhijie12.jpeg";
        File file = new File(pathName);
        FileInputStream fileInputStream = new FileInputStream(file);
        FileVo fileVo = new FileVo();
        HoneyStream honeyStream = new HoneyStream(fileInputStream);
        fileVo.setHoneyStream(honeyStream);
        FileVo fileVo1 = BeanConvertUtil.convertFileVo(file);
        InputStream inputStream = fileVo1.getHoneyStream().getInputStream();
        honeyMiniO.upload(bucketName, "yangzhijie9999.jpeg", inputStream, MediaType.IMAGE_JPEG);
    }

}
