package com.eboy.honey.oss.strategy;

import java.io.File;

/**
 * @author yangzhijie
 * @date 2020/11/4 15:40
 */
public interface SecondTransStrategy {


    /**
     * 秒传判断
     *
     * @param bucketName 桶名
     * @param file       文件实体
     * @return 是否是秒传
     */
    boolean conditionOnSecondTrans(String bucketName, File file);
}
