package com.eboy.honey.oss.server.application.service.impl;

import com.eboy.honey.oss.constant.FileState;
import com.eboy.honey.oss.server.application.BeanConvertUtil;
import com.eboy.honey.oss.server.application.dao.FileShardMapper;
import com.eboy.honey.oss.server.application.po.FileShardPo;
import com.eboy.honey.oss.server.application.service.FileShardService;
import com.eboy.honey.oss.server.application.vo.ConcurrentShardVo;
import com.eboy.honey.oss.server.application.vo.FileShardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yangzhijie
 * @date 2020/7/31 16:31
 */
@Service
public class FileShardServiceImpl implements FileShardService {

    @Autowired
    private FileShardMapper fileShardMapper;

    /**
     * 添加文件分片信息
     *
     * @param fileShardVo 文件分片信息
     * @return 是否成功
     */
    @Override
    public boolean addFileShard(FileShardVo fileShardVo) {
        return fileShardMapper.addFileShard(BeanConvertUtil.convert(fileShardVo, FileShardPo.class));
    }

    /**
     * 更新文件分片状态
     *
     * @param fileShardId    文件分片id
     * @param fileShardState 文件封片状态
     * @return 是否成功
     */
    @Override
    public boolean updateFileShardState(String fileShardId, FileState fileShardState) {
        return fileShardMapper.updateFileShardState(fileShardId, fileShardState.getStateCode());
    }

    /**
     * 根据文件fileKey获取分片信息
     *
     * @param fileKeys 文件fileKeys
     * @return 文件分片信息
     */
    @Override
    public Map<String, List<FileShardVo>> getFileShardInfoByFileKeys(List<String> fileKeys) {
        List<FileShardPo> fileShards = fileShardMapper.getFileShardInfoByFileKeys(fileKeys);
        List<FileShardVo> fileShardVos = BeanConvertUtil.convertByList(fileShards, FileShardVo.class);
        return fileShardVos.parallelStream().collect(Collectors.groupingBy(FileShardVo::getFileKey, LinkedHashMap::new, Collectors.toList()));
    }

    /**
     * 根据fileKey与分片状态获取分片信息
     *
     * @param fileKey        文件fileKey
     * @param fileShardState 分片状态
     * @return 文件分片信息
     */
    @Override
    public List<FileShardVo> getFileShardByFileKeyAndState(String fileKey, FileState fileShardState) {
        List<FileShardPo> rs = fileShardMapper.getFileShardByFileKeyAndState(fileKey, fileShardState.getStateCode());
        return BeanConvertUtil.convertByList(rs, FileShardVo.class);
    }

    /**
     * 根据fileKey获取当前要上传的分片
     *
     * @param fileKey 文件fileKey
     * @return 当前上传的分片
     */
    @Override
    public ConcurrentShardVo getConcurrentUploadShardIndex(String fileKey) {

        FileShardPo lastUploadShard = fileShardMapper.getLastUploadShardIndex(fileKey);
        int shardState = lastUploadShard.getShardState();
        ConcurrentShardVo concurrentShardVo = new ConcurrentShardVo();
        concurrentShardVo.setFileKey(lastUploadShard.getFileKey());
        // 如果上一块是在上传中或者成功了 则上传下一块
        if (shardState == FileState.UPLOADING.getStateCode() || shardState == FileState.SUCCESS.getStateCode()) {
            concurrentShardVo.setShardIndex(lastUploadShard.getShardIndex() + 1);
            return concurrentShardVo;
        }
        // 如果上一块是失败的，则继续上传这一块
        return BeanConvertUtil.convert(lastUploadShard, ConcurrentShardVo.class);
    }
}
