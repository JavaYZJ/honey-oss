package red.honey.oss.server.application.vo;

import lombok.Data;
import red.honey.oss.api.dto.HoneyStream;

import java.util.Date;

/**
 * @author yangzhijie
 * @date 2020/7/31 15:17
 */
@Data
public class FileShardVo {

    /**
     * 唯一id
     */
    private String uid;
    /**
     * 文件唯一标志
     */
    private String fileKey;
    /**
     * 分片文件名
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
     * 文件分片流
     */
    private HoneyStream honeyStream;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
