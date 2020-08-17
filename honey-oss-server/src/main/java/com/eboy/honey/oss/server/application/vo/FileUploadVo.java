package com.eboy.honey.oss.server.application.vo;

import lombok.Data;

import java.io.File;

/**
 * @author yangzhijie
 * @date 2020/8/17 13:37
 */
@Data
public class FileUploadVo {

    /**
     * file
     */
    private File file;
    /**
     * 分片大小
     */
    private int shardSize;
    /**
     * 分片总数
     */
    private int shardTotal;


}
