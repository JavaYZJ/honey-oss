package com.eboy.honey.oss.service.impl;

import com.eboy.honey.oss.dubbo.FileRpcService;
import com.eboy.honey.oss.dubbo.FileShardRpcService;
import com.eboy.honey.oss.service.HoneyOss;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author yangzhijie
 * @date 2020/8/13 16:20
 */
public class HoneyOssImpl implements HoneyOss {

    @Reference(version = "1.0")
    private FileRpcService fileRpcService;

    @Reference(version = "1.0")
    private FileShardRpcService fileShardRpcService;


    /**
     * 文件上传
     *
     * @param filePath    文件路径
     * @param contentType contentType
     */
    @Override
    public void upload(String filePath, ContentType contentType) {
        File file = new File(filePath);
        upload(file, contentType);
    }

    /**
     * 文件上传
     *
     * @param inputStream 文件流
     * @param contentType contentType
     */
    @Override
    public void upload(InputStream inputStream, ContentType contentType) {

    }

    /**
     * 文件上传
     *
     * @param file        文件
     * @param contentType contentType
     */
    @Override
    public void upload(File file, ContentType contentType) {
        try {
            InputStream inputStream = new FileInputStream(file);
            upload(inputStream, contentType);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("file not found");
        }
    }

    @Override
    public void downAsUrl() {

    }

    @Override
    public void downAsStream() {

    }
}
