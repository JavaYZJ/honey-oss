package com.eboy.honey.oss.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author yangzhijie
 * @date 2020/7/31 14:38
 */
public class FileShardDto implements Serializable {

    private static final long serialVersionUID = -4068352578784727481L;
    /**
     * 唯一id
     */
    @NotEmpty
    private String uid;
    /**
     * 文件唯一标志
     */
    @NotEmpty
    private String fileKey;
    /**
     * 分片文件名
     */
    @NotEmpty
    private String shardName;
    /**
     * 当前分片
     */
    @NotEmpty
    private int shardIndex;
    /**
     * 分片状态 0-上传中 1-上传成功 2-上传失败
     */
    private int shardState;
    /**
     * 分片流
     */
    @NotNull
    private HoneyStream honeyStream;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

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

    public HoneyStream getHoneyStream() {
        return honeyStream;
    }

    public void setHoneyStream(HoneyStream honeyStream) {
        this.honeyStream = honeyStream;
    }

    public String getShardName() {
        return shardName;
    }

    public void setShardName(String shardName) {
        this.shardName = shardName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
