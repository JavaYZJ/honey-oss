package com.eboy.honey.oss.server.application.service;

import com.eboy.honey.oss.entiy.Thumbnail;
import com.sun.istack.internal.NotNull;
import org.springframework.http.MediaType;

import java.io.File;

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
     * 构建默认缩略图规则
     *
     * @return 默认缩略图规则
     */
    default Thumbnail defaultThumbnail() {

        return new Thumbnail();

    }

}
