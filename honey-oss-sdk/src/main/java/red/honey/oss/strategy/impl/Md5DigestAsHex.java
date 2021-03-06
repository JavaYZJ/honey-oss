package red.honey.oss.strategy.impl;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.dubbo.config.annotation.Reference;
import red.honey.oss.api.constant.FileState;
import red.honey.oss.api.service.dubbo.FileRpcService;
import red.honey.oss.api.utils.HoneyFileUtil;
import red.honey.oss.strategy.SecondTransStrategy;

import java.io.File;
import java.util.Collections;

/**
 * @author yangzhijie
 * @date 2020/11/4 16:33
 */
public class Md5DigestAsHex implements SecondTransStrategy {

    @Reference(version = "1.0")
    private FileRpcService fileRpcService;

    /**
     * 秒传判断
     *
     * @param bucketName 桶名
     * @param file       文件实体
     * @return 是否是秒传
     */
    @SneakyThrows
    @Override
    public boolean conditionOnSecondTrans(String bucketName, File file) {
        String fileKey = HoneyFileUtil.getFileKey(FileUtils.openInputStream(file));
        return fileRpcService.getFileByFileKeys(Collections.singletonList(fileKey))
                .stream()
                .anyMatch(
                        e -> e.getFileState() == FileState.SUCCESS.getStateCode() && bucketName.equals(e.getBucketName())
                );

    }
}
