package com.eboy.honey.oss.api.dto;

import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/7/29 17:03
 */
public class FileDto implements Serializable {

    private static final long serialVersionUID = -23861453836665262L;
    /**
     * 唯一id
     */
    @NotEmpty
    private String uid;
    /**
     * 文件名
     */
    @NotEmpty
    private String fileName;
    /**
     * 文件key标志
     */
    @NotEmpty
    private String fileKey;
    /**
     * 文件后缀
     */
    private String fileSuffix;
    /**
     * 文件大小
     */
    @NotEmpty
    private Long fileSize;
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
     * 所属桶名
     */
    @NotEmpty
    private String bucketName;
    /**
     * 文件状态 0-上传中 1-上传成功 2-上传失败
     */
    private int fileState;
    /**
     * 文件
     */
    private File file;

    /**
     * 文件流
     */
    private HoneyStream honeyStream;

    /**
     * 分片信息
     */
    private List<FileShardDto> fileShardDtos;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getShardSize() {
        return shardSize;
    }

    public void setShardSize(long shardSize) {
        this.shardSize = shardSize;
    }

    public int getShardTotal() {
        return shardTotal;
    }

    public void setShardTotal(int shardTotal) {
        this.shardTotal = shardTotal;
    }

    public List<FileShardDto> getFileShardDtos() {
        return fileShardDtos;
    }

    public void setFileShardDtos(List<FileShardDto> fileShardDtos) {
        this.fileShardDtos = fileShardDtos;
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

    public HoneyStream getHoneyStream() {
        return honeyStream;
    }

    public void setHoneyStream(HoneyStream honeyStream) {
        this.honeyStream = honeyStream;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public Integer getFileState() {
        return fileState;
    }

    public void setFileState(Integer fileState) {
        this.fileState = fileState;
    }
}
