package com.eboy.honey.oss.server.api.dto;

import java.io.Serializable;

/**
 * @author yangzhijie
 * @date 2020/7/31 14:38
 */
public class FileShardDto implements Serializable {

    /**
     * 唯一id
     */
    private String uid;
    /**
     * 文件唯一标志
     */
    private String fileKey;
    /**
     * 当前分片
     */
    private int shardIndex;
    /**
     * 分片状态 0-上传中 1-上传成功 2-上传失败
     */
    private int shardState;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public int getShardIndex() {
        return shardIndex;
    }

    public void setShardIndex(int shardIndex) {
        this.shardIndex = shardIndex;
    }

    public int getShardState() {
        return shardState;
    }

    public void setShardState(int shardState) {
        this.shardState = shardState;
    }
}
