package com.eboy.honey.oss.service;

import org.apache.http.entity.ContentType;

import java.io.File;
import java.io.InputStream;

/**
 * @author yangzhijie
 * @date 2020/8/13 16:08
 */
public interface HoneyOss {

    /**
     * 文件上传
     *
     * @param filePath    文件路径
     * @param contentType contentType
     */
    void upload(String filePath, ContentType contentType);

    /**
     * 文件上传
     *
     * @param inputStream 文件流
     * @param contentType contentType
     */
    void upload(InputStream inputStream, ContentType contentType);

    void upload(File file, ContentType contentType);

    void downAsUrl();

    void downAsStream();

}
