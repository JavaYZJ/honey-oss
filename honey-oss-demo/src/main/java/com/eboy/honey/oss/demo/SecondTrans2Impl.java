package com.eboy.honey.oss.demo;

import com.eboy.honey.oss.strategy.SecondTransStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author yangzhijie
 * @date 2020/11/6 9:43
 */
@Service
@Slf4j
public class SecondTrans2Impl implements SecondTransStrategy {
    /**
     * 秒传判断
     *
     * @param bucketName 桶名
     * @param file       文件实体
     * @return 是否是秒传
     */
    @Override
    public boolean conditionOnSecondTrans(String bucketName, File file) {
        return false;
    }
}
