package red.honey.oss.server.application.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import red.honey.oss.server.application.po.FileShardPo;

import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/7/29 15:44
 */
@Mapper
public interface FileShardMapper {


    /**
     * 添加文件分片信息
     *
     * @param fileShardPo 文件分片信息
     * @return 是否成功
     */
    boolean addFileShard(@Param("fileShard") FileShardPo fileShardPo);

    /**
     * 更新文件分片状态
     *
     * @param fileShardId    文件分片id
     * @param fileShardState 文件封片状态
     * @return 是否成功
     */
    boolean updateFileShardState(@Param("fileShardId") String fileShardId, @Param("fileShardState") int fileShardState);

    /**
     * 根据文件fileKey获取分片信息
     *
     * @param fileKeys 文件fileKeys
     * @return 文件分片信息
     */
    List<FileShardPo> getFileShardInfoByFileKeys(@Param("fileKeys") List<String> fileKeys);

    /**
     * 根据fileKey与分片状态获取分片信息
     *
     * @param fileKey   文件fileKey
     * @param fileState 分片状态
     * @return 文件分片信息
     */
    List<FileShardPo> getFileShardByFileKeyAndState(@Param("fileKey") String fileKey, @Param("fileState") int fileState);

    /**
     * 根据fileKey获取最后一次上传的分片索引
     *
     * @param fileKey 文件fileKey
     * @return 当前上传的分片
     */
    FileShardPo getLastUploadShardIndex(@Param("fileKey") String fileKey);
}
