package com.eboy.honey.oss.server.application.utils;

import com.eboy.honey.oss.dto.HoneyStream;
import com.eboy.honey.oss.server.application.vo.FileVo;
import com.eboy.honey.oss.utils.HoneyFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/7/31 15:27
 */
@Slf4j
public class BeanConvertUtil {

    /**
     * bean 的dto vo po 转换器
     *
     * @param source 源
     * @param clazz  目标class
     */
    public static <S, T> T convert(S source, Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            BeanUtils.copyProperties(source, instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("bean convert fail");
        }

    }

    /**
     * list bean 的dto vo po 转换器
     *
     * @param source 源
     * @param clazz  目标class
     */
    public static <T, S> List<T> convertByList(List<S> source, Class<T> clazz) {
        List<T> ts = new ArrayList<>();
        for (S s : source) {
            ts.add(convert(s, clazz));
        }
        return ts;
    }

    /**
     * File转换成FileVo
     *
     * @param file file
     * @return fileVo
     */
    public static FileVo convertFileVo(File file) {
        try {
            FileVo fileVo = new FileVo();
            FileInputStream inputStream = FileUtils.openInputStream(file);
            // 设置uid
            fileVo.setUid(HoneyFileUtil.get32Uid());
            // 设置流
            HoneyStream honeyStream = new HoneyStream(inputStream);
            fileVo.setHoneyStream(honeyStream);
            // 设置文件名
            String fileName = file.getName();
            fileVo.setFileName(fileName);
            // 设置fileKey
            String fileKey = HoneyFileUtil.getFileKey(FileUtils.openInputStream(file));
            fileVo.setFileKey(fileKey);
            // 设置文件格式
            String fileSuffix = HoneyFileUtil.getFileSuffix(fileName);
            fileVo.setFileSuffix(fileSuffix);
            // 设置文件大小
            long fileSize = HoneyFileUtil.getFileSize(inputStream);
            fileVo.setFileSize(fileSize);
            // 设置分片总数
            fileVo.setShardTotal(0);
            // 设置分片大小
            fileVo.setShardSize(0);
            return fileVo;
        } catch (IOException e) {
            log.warn("主服务构建FileVo失败，原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
