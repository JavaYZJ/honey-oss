package com.eboy.honey.oss.api.dto;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/8/14 13:56
 */
public class FileUpload implements Serializable {

    private static final long serialVersionUID = -3852130778547439783L;

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

    /**
     * 分片信息
     */
    private List<FileShardDto> fileShards;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getShardSize() {
        return shardSize;
    }

    public void setShardSize(int shardSize) {
        this.shardSize = shardSize;
    }

    public int getShardTotal() {
        return shardTotal;
    }

    public void setShardTotal(int shardTotal) {
        this.shardTotal = shardTotal;
    }

    public List<FileShardDto> getFileShards() {
        return fileShards;
    }

    public void setFileShards(List<FileShardDto> fileShards) {
        this.fileShards = fileShards;
    }
}
