package com.eboy.honey.oss.server.application.service;

import com.eboy.honey.oss.server.application.constant.FileState;
import com.eboy.honey.oss.server.application.vo.ConcurrentShardVo;
import com.eboy.honey.oss.server.application.vo.FileShardVo;

import java.util.List;
import java.util.Map;

/**
 * @author yangzhijie
 * @date 2020/7/31 14:33
 */
public interface FileShardService {

    /**
     * 添加文件分片信息
     *
     * @param fileShardVo 文件分片信息
     * @return 是否成功
     */
    boolean addFileShard(FileShardVo fileShardVo);

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
    Map<String, List<FileShardVo>> getFileShardInfoByFileKeys(List<String> fileKeys);

    /**
     * 根据fileKey与分片状态获取分片信息
     *
     * @param fileKey   文件fileKey
     * @param fileShardState 分片状态
     * @return 文件分片信息
     */
    List<FileShardVo> getFileShardByFileKeyAndState(String fileKey, FileState fileShardState);


    /**
     * 根据fileKey获取当前要上传的分片
     *
     * @param fileKey 文件fileKey
     * @return 当前上传的分片
     */
    ConcurrentShardVo getConcurrentUploadShardIndex(String fileKey);
}
