package com.eboy.honey.oss.server.application.vo;

import lombok.Data;

/**
 * @author yangzhijie
 * @date 2020/7/31 17:17
 */
@Data
public class ConcurrentShardVo {

    /**
     * 文件唯一标志
     */
    private String fileKey;
    /**
     * 当前分片
     */
    private int shardIndex;
}
