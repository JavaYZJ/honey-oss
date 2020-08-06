package com.eboy.honey.oss.server.client;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author yangzhijie
 * @date 2020/7/28 16:22
 */
@Slf4j
@Component
public class HoneyMiniO {

    @Autowired
    MinioClient minioClient;

    @Value("${honey.oss.minio.bucketName}")
    private String bucketName;


    /**
     * 上传
     *
     * @param bucketName  桶名
     * @param objectName  对象名
     * @param filePath    文件路径
     * @param contentType contentTyp
     */
    public void upload(String bucketName, String objectName, String filePath, ContentType contentType) {
        try {
            checkBucket(bucketName);
            minioClient.putObject(bucketName, objectName, filePath, contentType.toString());
        } catch (Exception e) {
            log.warn("上传失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传
     *
     * @param objectName  对象名
     * @param filePath    文件路径
     * @param contentType contentType
     */
    public void upload(String objectName, String filePath, ContentType contentType) {
        try {
            checkBucket(bucketName);
            minioClient.putObject(bucketName, objectName, filePath, contentType.toString());
        } catch (Exception e) {
            log.warn("上传失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传
     *
     * @param bucketName  桶名
     * @param objectName  对象名
     * @param inputStream 输入流
     * @param contentType contentType
     */
    public void upload(String bucketName, String objectName, InputStream inputStream, ContentType contentType) {
        try {
            checkBucket(bucketName);
            minioClient.putObject(bucketName, objectName, inputStream, contentType.toString());
        } catch (Exception e) {
            log.warn("上传失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传
     *
     * @param objectName  对象名
     * @param inputStream 输入流
     * @param contentType contentType
     */
    public void upload(String objectName, InputStream inputStream, ContentType contentType) {
        try {
            checkBucket(bucketName);
            minioClient.putObject(bucketName, objectName, inputStream, contentType.toString());
        } catch (Exception e) {
            log.warn("上传失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传
     *
     * @param objectName  对象名
     * @param inputStream 输入流
     * @param contentType contentType
     * @return string url
     */
    public String uploadAndGetUrl(String objectName, InputStream inputStream, ContentType contentType) {
        try {
            checkBucket(bucketName);
            minioClient.putObject(bucketName, objectName, inputStream, contentType.toString());
            return minioClient.getObjectUrl(bucketName, objectName);
        } catch (Exception e) {
            log.warn("上传失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传
     *
     * @param bucketName  桶名
     * @param objectName  对象名
     * @param inputStream 输入流
     * @param contentType contentType
     * @return string url 直接点击该url即可在浏览器中下载文件
     */
    public String uploadAndGetUrl(String bucketName, String objectName, InputStream inputStream, ContentType contentType) {
        try {
            checkBucket(bucketName);
            minioClient.putObject(bucketName, objectName, inputStream, contentType.toString());
            return minioClient.getObjectUrl(bucketName, objectName);
        } catch (Exception e) {
            log.warn("上传失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载文件至指定的本地文件路径
     *
     * @param bucketName   桶名
     * @param fileName     文件名
     * @param fileDownPath 文件存储路径
     */
    public void down2Local(String bucketName, String fileName, String fileDownPath) {
        try {
            minioClient.getObject(bucketName, fileName, fileDownPath);
        } catch (Exception e) {
            log.warn("下载失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载文件至指定的本地文件路径
     *
     * @param fileName     文件名
     * @param fileDownPath 文件存储路径
     */
    public void down2Local(String fileName, String fileDownPath) {
        try {
            minioClient.getObject(bucketName, fileName, fileDownPath);
        } catch (Exception e) {
            log.warn("下载失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载为文件流
     *
     * @param bucketName 桶名
     * @param objectName 对象名
     * @return InputStream 文件流
     */
    public InputStream downAsStream(String bucketName, String objectName) {
        try {
            return minioClient.getObject(bucketName, objectName);
        } catch (Exception e) {
            log.warn("下载失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载为文件流
     *
     * @param objectName 对象名
     * @return InputStream 文件流
     */
    public InputStream downAsStream(String objectName) {
        try {
            return minioClient.getObject(bucketName, objectName);
        } catch (Exception e) {
            log.warn("下载失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载为url
     *
     * @param bucketName 桶名
     * @param objectName 对象名
     * @return string 文件的url
     */
    public String downAsUrl(String bucketName, String objectName) {
        try {
            return minioClient.presignedGetObject(bucketName, objectName);
        } catch (Exception e) {
            log.warn("下载失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载为url
     *
     * @param objectName 对象名
     * @return string 文件的url
     */
    public String downAsUrl(String objectName) {
        try {
            return minioClient.presignedGetObject(bucketName, objectName);
        } catch (Exception e) {
            log.warn("下载失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private void checkBucket(String bucketName) throws Exception {
        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(bucketName);
        if (isExist) {
            System.out.println("Bucket already exists.");
        } else {
            // 创建一个名为bucketName的存储桶，用于存储文件。
            minioClient.makeBucket(bucketName);
        }

    }
}
