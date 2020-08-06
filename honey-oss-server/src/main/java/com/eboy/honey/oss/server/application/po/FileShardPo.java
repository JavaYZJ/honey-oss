package com.eboy.honey.oss.server.application.po;

import lombok.Data;

import java.util.Date;

/**
 * @author yangzhijie
 * @date 2020/7/31 13:39
 */
@Data
public class FileShardPo {
    /**
     * 唯一id
     */
    private String uid;
    /**
     * 文件唯一标志
     */
    private String fileKey;
    /**
     * 文件分片名
     */
    private String shardName;
    /**
     * 当前分片
     */
    private int shardIndex;
    /**
     * 分片状态 0-上传中 1-上传成功 2-上传失败
     */
    private int shardState;
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
