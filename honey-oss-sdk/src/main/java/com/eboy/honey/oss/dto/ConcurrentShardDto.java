package com.eboy.honey.oss.dto;

import java.io.Serializable;

/**
 * @author yangzhijie
 * @date 2020/8/6 13:37
 */
public class ConcurrentShardDto implements Serializable {

    private static final long serialVersionUID = 6624833228997817318L;
    /**
     * 文件唯一标志
     */
    private String fileKey;
    /**
     * 当前分片
     */
    private int shardIndex;

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
}
