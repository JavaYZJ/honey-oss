package red.honey.oss.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import red.honey.oss.api.dto.FileDto;
import red.honey.oss.api.dto.HoneyStream;
import red.honey.oss.api.utils.HoneyFileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/11/4 15:12
 */
@Slf4j
public class BeanConverter {


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
