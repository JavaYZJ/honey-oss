package com.eboy.honey.oss.utils;

import com.eboy.honey.oss.api.dto.FileDto;
import com.eboy.honey.oss.api.dto.HoneyStream;
import com.eboy.honey.oss.api.utils.HoneyFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author yangzhijie
 * @date 2020/11/4 15:12
 */
@Slf4j
public class BeanConverter {


    public static FileDto convert2FileDto(File file) {

        FileDto fileDto = new FileDto();
        try {
            FileInputStream inputStream = FileUtils.openInputStream(file);
            FileInputStream inputStreamTemp = FileUtils.openInputStream(file);
            // 设置uid
            fileDto.setUid(HoneyFileUtil.get32Uid());
            // 设置流
            HoneyStream honeyStream = new HoneyStream(inputStream);
            fileDto.setHoneyStream(honeyStream);
            // 设置文件名
            String fileName = file.getName();
            fileDto.setFileName(fileName);
            // 设置fileKey
            String fileKey = HoneyFileUtil.getFileKey(inputStreamTemp);
            fileDto.setFileKey(fileKey);
            // 设置文件格式
            String fileSuffix = HoneyFileUtil.getFileSuffix(fileName);
            fileDto.setFileSuffix(fileSuffix);
            // 设置文件大小
            long fileSize = HoneyFileUtil.getFileSize(inputStream);
            fileDto.setFileSize(fileSize);
            // 设置分片总数
            fileDto.setShardTotal(0);
            // 设置分片大小
            fileDto.setShardSize(0);
            return fileDto;
        } catch (Exception e) {
            log.warn("SDK构建FileDto失败，原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
