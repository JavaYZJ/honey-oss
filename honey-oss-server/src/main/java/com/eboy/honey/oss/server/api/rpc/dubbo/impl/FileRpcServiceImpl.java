package com.eboy.honey.oss.server.api.rpc.dubbo.impl;


import com.eboy.honey.oss.api.constant.FileState;
import com.eboy.honey.oss.api.dto.FileDto;
import com.eboy.honey.oss.api.dto.FileShardDto;
import com.eboy.honey.oss.api.dto.HoneyStream;
import com.eboy.honey.oss.api.service.dubbo.FileRpcService;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.utils.BeanConvertUtil;
import com.eboy.honey.oss.server.application.vo.FileVo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangzhijie
 * @date 2020/8/6 13:38
 */
@Service(version = "1.0")
public class FileRpcServiceImpl implements FileRpcService {

    @Autowired
    private FileService fileService;

    /**
     * 下载为url
     *
     * @param bucketName 桶名
     * @param fileKey    对象名
     * @return string 文件的url
     */
    @Override
    public String downAsUrl(String bucketName, String fileKey) {
        return fileService.downAsUrl(bucketName, fileKey);
    }

    /**
     * 下载为url
     *
     * @param bucketName 桶名
     * @param fileKey    文件fileKey
     * @param expires    过期时间(秒)
     * @return string 文件的url
     */
    @Override
    public String downAsUrl(String bucketName, String fileKey, Integer expires) {
        return fileService.downAsUrl(bucketName, fileKey, expires);
    }

    /**
     * 下载为文件流
     *
     * @param bucketName 桶名
     * @param fileKey    fileKey
     * @return InputStream 文件流
     */
    @Override
    public HoneyStream downAsStream(String bucketName, String fileKey) {
        return fileService.downAsHoneyStream(bucketName, fileKey);
    }


    /**
     * 根据ids查找文件
     *
     * @param ids 文件ids
     * @return 文件
     */
    @Override
    public List<FileDto> getFileByIds(List<String> ids) {
        List<FileVo> files = fileService.getFileByIds(ids);
        return BeanConvertUtil.convertByList(files, FileDto.class);
    }

    /**
     * 根据fileKey查找文件
     *
     * @param fileKeys 文件keys
     * @return 文件
     */
    @Override
    public List<FileDto> getFileByFileKeys(List<String> fileKeys) {
        List<FileVo> files = fileService.getFileByFileKeys(fileKeys);
        return vo2Dto(files);
    }

    /**
     * 更新文件名
     *
     * @param fileId   文件id
     * @param fileName 文件名
     * @return 是否成功
     */
    @Override
    public boolean updateFileName(String fileId, String fileName) {
        return fileService.updateFileName(fileId, fileName);
    }

    /**
     * 根据ids删除文件
     *
     * @param ids 文件ids
     * @return 是否删除成功
     */
    @Override
    public boolean deleteFileByIds(List<String> ids) {
        return fileService.deleteFileByIds(ids);
    }

    /**
     * 根据fileKeys删除文件
     *
     * @param fileKeys 文件keys
     * @return 是否成功
     */
    @Override
    public boolean deletedFileByFileKeys(List<String> fileKeys) {
        return fileService.deletedFileByFileKeys(fileKeys);
    }

    /**
     * 更新文件状态
     *
     * @param fileKey   文件key
     * @param fileState 文件状态
     * @return 是否成功
     */
    @Override
    public boolean updateFileState(String fileKey, FileState fileState) {
        return fileService.updateFileState(fileKey, fileState);
    }

    private List<FileDto> vo2Dto(List<FileVo> list) {
        return list.stream().map(e -> {
            FileDto dto = BeanConvertUtil.convert(e, FileDto.class);
            if (e.getFileShardVos() != null) {
                dto.setFileShardDtos(BeanConvertUtil.convertByList(e.getFileShardVos(), FileShardDto.class));
            }
            return dto;
        }).collect(Collectors.toList());
    }
}
