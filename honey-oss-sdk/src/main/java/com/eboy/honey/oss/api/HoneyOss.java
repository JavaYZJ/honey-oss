package com.eboy.honey.oss.api;

import com.eboy.honey.oss.annotation.SecondTrans;
import com.eboy.honey.oss.api.constant.FileState;
import com.eboy.honey.oss.api.dto.FileDto;
import com.eboy.honey.oss.api.service.dubbo.FileRpcService;
import com.eboy.honey.oss.api.service.dubbo.FileShardRpcService;
import com.eboy.honey.oss.api.service.dubbo.PureFileRpcService;
import com.eboy.honey.oss.api.utils.HoneyFileUtil;
import com.eboy.honey.oss.client.HoneyMiniO;
import com.eboy.honey.oss.utils.BeanConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.io.File;

/**
 * @author yangzhijie
 * @date 2020/11/4 10:41
 */
@Slf4j
public class HoneyOss {

    /**
     * 来自honey-oss-server的文件服务
     */
    @Reference(version = "1.0")
    private FileRpcService fileRpcService;
    /**
     * 来自honey-oss-server的文件分片服务
     */
    @Reference(version = "1.0")
    private FileShardRpcService fileShardRpcService;

    @Reference(version = "1.0")
    private PureFileRpcService postFileRpcService;

    @Autowired
    private HoneyMiniO honeyMiniO;


    /**
     * 上传文件
     * <p>
     * 默认会自动分片上传，支持单个最大文件为5T
     * 但是这种情况的分片不会保存在honey-oss上
     *
     * @param file        文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @return fileKey
     */
    @SecondTrans(bucketName = "${honey.oss.bucketName}")
    public String upload(File file, String bucketName, MediaType contentType) {
        // file convert to fileDto
        FileDto fileDto = BeanConverter.convert2FileDto(file);
        fileDto.setBucketName(bucketName);
        fileDto.setFileState(FileState.SUCCESS.getStateCode());
        postFileRpcService.postFileInfo(fileDto);
        // upload to MiniO
        String objectName = HoneyFileUtil.buildObjectNameByFileKey(fileDto.getFileName(), fileDto.getFileKey());
        honeyMiniO.upload(bucketName, objectName, fileDto.getHoneyStream().getInputStream(), contentType);
        return fileDto.getFileKey();
    }


    public String downAsUrl(String bucketName, String fileKey) {
        return fileRpcService.downAsUrl(bucketName, fileKey);
    }

}
