package com.eboy.honey.oss.service.impl;

import com.eboy.honey.oss.dto.FileDto;
import com.eboy.honey.oss.dubbo.FileRpcService;
import com.eboy.honey.oss.dubbo.FileShardRpcService;
import com.eboy.honey.oss.service.HoneyOss;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.http.entity.ContentType;

import static com.eboy.honey.oss.utils.HoneyFileUtil.buildArgs;

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
     * @param fileDto     文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     */
    @Override
    public void upload(FileDto fileDto, String bucketName, ContentType contentType) {
        buildArgs(fileDto);
        fileDto.setShardTotal(0);
        fileDto.setShardSize(0);
        fileRpcService.uploadFile(fileDto, bucketName, contentType);
    }


    @Override
    public void downAsUrl() {

    }

    @Override
    public void downAsStream() {

    }

}
