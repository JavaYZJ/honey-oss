package com.eboy.honey.oss.server.application.service;

import com.eboy.honey.oss.constant.ImageType;
import com.eboy.honey.oss.entiy.OutputMode;
import com.eboy.honey.oss.entiy.Thumbnail;
import com.eboy.honey.oss.server.application.utils.FilePathUtil;
import com.sun.istack.internal.NotNull;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.InputStream;

/**
 * 缩略图服务
 *
 * @author yangzhijie
 * @date 2020/8/21 10:44
 */

public interface ThumbnailService {

    /**
     * 构建缩略图
     *
     * @param thumbnail 构建规则
     */
    void buildThumbnail(@NotNull Thumbnail thumbnail);

    /**
     * 上传缩略图
     *
     * @param image       缩略图
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 缩略图fileKey
     */
    String upload(File image, String bucketName, MediaType contentType);


    /**
     * 根据原图获取缩略图链接
     *
     * @param fileKey    原图fileKey
     * @param bucketName 桶名
     * @return 缩略图链接
     */
    String getUrlByOriginalPicture(String bucketName, String fileKey);


    /**
     * 根据原图获取缩略图链接
     *
     * @param fileKey    原图fileKey
     * @param bucketName 桶名
     * @param expires    过期时间（秒）
     * @return 缩略图链接
     */
    String getUrlByOriginalPicture(String bucketName, String fileKey, Integer expires);


    /**
     * 根据原图获取缩略图流
     *
     * @param bucketName 桶名
     * @param fileKey    原图fileKey
     * @return 缩略图流
     */
    InputStream getStreamByOriginalPicture(String bucketName, String fileKey);


    /**
     * 根据原图将缩略图下载至指定本地路径
     *
     * @param bucketName   桶名
     * @param fileKey      原图fileKey
     * @param fileDownPath 指定本地路径
     */
    void down2LocalByOriginalPicture(String bucketName, String fileKey, String fileDownPath);


    /**
     * 构建默认缩略图规则
     *
     * @param image 原图
     * @return 默认缩略图规则
     */
    default Thumbnail defaultThumbnail(File image) {
        String thumbnailPath = FilePathUtil.defaultThumbnailPath() + ImageType.JPEG;
        Thumbnail thumbnail = new Thumbnail();
        OutputMode outputMode = new OutputMode();
        outputMode.setFilePath(thumbnailPath);
        thumbnail.setOutputMode(outputMode);
        thumbnail.setInputSource(image);
        buildThumbnail(thumbnail);
        return thumbnail;
    }

}
