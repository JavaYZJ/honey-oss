package com.eboy.honey.oss.service;

import com.eboy.honey.oss.dto.FileDto;
import org.apache.http.entity.ContentType;

import java.io.IOException;

/**
 * @author yangzhijie
 * @date 2020/8/13 16:08
 */
public interface HoneyOss {

    /**
     * 文件上传
     *
     * @param fileDto     文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     */
    void upload(FileDto fileDto, String bucketName, ContentType contentType) throws IOException;

    void downAsUrl();

    void downAsStream();

}
