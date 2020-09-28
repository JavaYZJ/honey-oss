package com.eboy.honey.oss.server.application.po;

import lombok.Data;

import java.util.Date;

/**
 * @author yangzhijie
 * @date 2020/7/29 15:33
 */
@Data
public class FilePo {

    /**
     * 自增id
     */
    private String id;
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
    private long fileSize;
    /**
     * 文件相对路径
     */
    private String filePath;
    /**
     * 分片大小
     */
    private long shardSize;
    /**
     * 分片总数
     */
    private int shardTotal;
    /**
     * 文件状态 0-上传中 1-上传成功 2-上传失败
     */
    private int fileState;
    /**
     * 所属桶名
     */
    private String bucketName;
    /**
     * 是否删除
     */
    private boolean isDeleted;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
