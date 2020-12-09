package red.honey.oss.server.application.vo;

import lombok.Data;
import red.honey.oss.api.dto.HoneyStream;

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
     * 所属桶名
     */
    private String bucketName;

    /**
     * 文件状态 0-上传中 1-上传成功 2-上传失败
     */
    private int fileState;
    /**
     * 流
     */
    private HoneyStream honeyStream;
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
