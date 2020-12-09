package red.honey.oss.api.service.dubbo;


import red.honey.oss.api.constant.FileState;
import red.honey.oss.api.dto.ConcurrentShardDto;
import red.honey.oss.api.dto.FileShardDto;

import java.util.List;
import java.util.Map;

/**
 * @author yangzhijie
 * @date 2020/8/6 13:34
 */
public interface FileShardRpcService {

    /**
     * 添加文件分片信息
     *
     * @param fileShardDto 文件分片信息
     * @return 是否成功
     */
    boolean addFileShard(FileShardDto fileShardDto);

    /**
     * 更新文件分片状态
     *
     * @param fileShardId    文件分片id
     * @param fileShardState 文件封片状态
     * @return 是否成功
     */
    boolean updateFileShardState(String fileShardId, FileState fileShardState);

    /**
     * 根据文件fileKey获取分片信息
     *
     * @param fileKeys 文件fileKeys
     * @return 文件分片信息
     */
    Map<String, List<FileShardDto>> getFileShardInfoByFileKeys(List<String> fileKeys);

    /**
     * 根据fileKey与分片状态获取分片信息
     *
     * @param fileKey        文件fileKey
     * @param fileShardState 分片状态
     * @return 文件分片信息
     */
    List<FileShardDto> getFileShardByFileKeyAndState(String fileKey, FileState fileShardState);


    /**
     * 根据fileKey获取当前要上传的分片
     *
     * @param fileKey 文件fileKey
     * @return 当前上传的分片
     */
    ConcurrentShardDto getConcurrentUploadShardIndex(String fileKey);
}
