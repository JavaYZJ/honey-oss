package com.eboy.honey.oss.server.api.rpc.dubbo.impl;


import com.eboy.honey.oss.constant.FileState;
import com.eboy.honey.oss.dto.ConcurrentShardDto;
import com.eboy.honey.oss.dto.FileShardDto;
import com.eboy.honey.oss.dubbo.service.FileShardRpcService;
import com.eboy.honey.oss.server.application.BeanConvertUtil;
import com.eboy.honey.oss.server.application.service.FileShardService;
import com.eboy.honey.oss.server.application.vo.FileShardVo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangzhijie
 * @date 2020/8/6 15:16
 */
@Service(version = "1.0")
public class FileShardRpcServiceImpl implements FileShardRpcService {

    @Autowired
    private FileShardService fileShardService;

    /**
     * 添加文件分片信息
     *
     * @param fileShardDto 文件分片信息
     * @return 是否成功
     */
    @Override
    public boolean addFileShard(FileShardDto fileShardDto) {
        FileShardVo fileShardVo = BeanConvertUtil.convert(fileShardDto, FileShardVo.class);
        return fileShardService.addFileShard(fileShardVo);
    }

    /**
     * 更新文件分片状态
     *
     * @param fileShardId    文件分片id
     * @param fileShardState 文件分片状态
     * @return 是否成功
     */
    @Override
    public boolean updateFileShardState(String fileShardId, FileState fileShardState) {
        return fileShardService.updateFileShardState(fileShardId, fileShardState);
    }

    /**
     * 根据文件fileKey获取分片信息
     *
     * @param fileKeys 文件fileKeys
     * @return 文件分片信息
     */
    @Override
    public Map<String, List<FileShardDto>> getFileShardInfoByFileKeys(List<String> fileKeys) {
        Map<String, List<FileShardVo>> rs = fileShardService.getFileShardInfoByFileKeys(fileKeys);
        Map<String, List<FileShardDto>> infoMap = new HashMap<>(rs.size());
        rs.forEach((key, value) -> infoMap.put(key, BeanConvertUtil.convertByList(value, FileShardDto.class)));
        return infoMap;
    }

    /**
     * 根据fileKey与分片状态获取分片信息
     *
     * @param fileKey        文件fileKey
     * @param fileShardState 分片状态
     * @return 文件分片信息
     */
    @Override
    public List<FileShardDto> getFileShardByFileKeyAndState(String fileKey, FileState fileShardState) {
        return null;
    }

    /**
     * 根据fileKey获取当前要上传的分片
     *
     * @param fileKey 文件fileKey
     * @return 当前上传的分片
     */
    @Override
    public ConcurrentShardDto getConcurrentUploadShardIndex(String fileKey) {
        return null;
    }
}
