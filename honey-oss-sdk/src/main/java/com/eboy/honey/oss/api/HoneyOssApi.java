package com.eboy.honey.oss.api;

import com.eboy.honey.oss.constant.FileState;
import com.eboy.honey.oss.dto.FileDto;
import com.eboy.honey.oss.dto.FileShardDto;
import com.eboy.honey.oss.dto.HoneyStream;
import com.eboy.honey.oss.dubbo.FileRpcService;
import com.eboy.honey.oss.dubbo.FileShardRpcService;
import com.eboy.honey.oss.utils.HoneyFileUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.http.MediaType;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

/**
 * @author yangzhijie
 * @date 2020/8/27 15:42
 */
public class HoneyOssApi {

    public static FileRpcService fileService;

    public static FileShardRpcService fileShardService;

    @Reference(version = "1.0")
    private FileRpcService fileRpcService;

    @Reference(version = "1.0")
    private FileShardRpcService fileShardRpcService;

    /**
     * 分块上传
     *
     * @param filePath 文件路径
     * @param count    分块数
     */
    public static void uploadByShard(String filePath, int count, String bucketName, MediaType contentType) {
        HoneyFileUtil.spiltFile(filePath, count);
        File all = new File(filePath);
        FileDto fileDto = HoneyFileUtil.convertFileDto(all);
        String fileName = fileDto.getFileName();
        for (int i = 0; i < count; i++) {
            try (FileInputStream fileInputStream = new FileInputStream(new File(filePath + "_" + i + ".tmp"))) {
                FileShardDto fileShardDto = buildFileShard(fileInputStream, fileName, i);
                fileDto.setFileShardDtos(Collections.singletonList(fileShardDto));
                fileService.uploadByShard(fileDto, bucketName, contentType);
            } catch (IOException e) {
                throw new RuntimeException("uploadByShard happen error");
            }
        }
    }

    private static FileDto decorateFileDto(FileDto fileDto, int shardTotal, long shardSize) {
        fileDto.setHoneyStream(null);
        fileDto.setShardTotal(shardTotal);
        fileDto.setShardSize(shardSize);
        return fileDto;
    }

    /**
     * 构建FileShard
     *
     * @param inputStream 分片流
     * @param fileName    文件名
     * @param shardIndex  当前分片索引
     * @return FileShard
     */
    private static FileShardDto buildFileShard(InputStream inputStream, String fileName, int shardIndex) {
        FileShardDto fileShardDto = new FileShardDto();
        String uid = HoneyFileUtil.get32Uid();
        fileShardDto.setUid(uid);
        String fileKey = HoneyFileUtil.getFileKey(inputStream);
        fileShardDto.setFileKey(fileKey);
        String shardName = HoneyFileUtil.buildShardName(fileName, shardIndex);
        fileShardDto.setShardName(shardName);
        fileShardDto.setShardIndex(shardIndex);
        fileShardDto.setShardState(FileState.UPLOADING.getStateCode());
        HoneyStream honeyStream = new HoneyStream(inputStream);
        fileShardDto.setHoneyStream(honeyStream);
        return fileShardDto;
    }

    @PostConstruct
    public void init() {
        fileService = fileRpcService;
        fileShardService = fileShardRpcService;
    }
}
