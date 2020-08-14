package com.eboy.honey.oss.server.application.vo;

import lombok.Data;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/7/31 15:16
 */
@Data
public class FileVo {

    /**
     * 唯一id
     */
    private String uid;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件key标志
     */
    private String fileKey;
    /**
     * 文件后缀
     */
    private String fileSuffix;
    /**
     * 文件大小
     */
    private int fileSize;
    /**
     * 文件相对路径
     */
    private String filePath;
    /**
     * 分片大小
     */
    private int shardSize;
    /**
     * 分片总数
     */
    private int shardTotal;
    /**
     * file
     */
    private File file;
    /**
     * 分片信息
     */
    private List<FileShardVo> fileShardVos;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
