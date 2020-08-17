package com.eboy.honey.oss.server.api.rpc.dubbo.impl;


import com.eboy.honey.oss.dto.FileDto;
import com.eboy.honey.oss.dubbo.FileRpcService;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.utils.BeanConvertUtil;
import com.eboy.honey.oss.server.application.vo.FileVo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/8/6 13:38
 */
@Service(version = "1.0")
public class FileRpcServiceImpl implements FileRpcService {

    @Value("${honey.oss.minio.bucketName}")
    private String bucketName;

    @Autowired
    private FileService fileService;


    /**
     * 根据ids查找文件
     *
     * @param ids 文件ids
     * @return 文件
     */
    @Override
    public List<FileDto> getFileByIds(List<String> ids) {
        List<FileVo> list = fileService.getFileByIds(ids);
        return BeanConvertUtil.convertByList(list, FileDto.class);
    }

    /**
     * 根据fileKey查找文件
     *
     * @param fileKeys 文件keys
     * @return 文件
     */
    @Override
    public List<FileDto> getFileByFileKeys(List<String> fileKeys) {
        List<FileVo> rs = fileService.getFileByFileKeys(fileKeys);
        return BeanConvertUtil.convertByList(rs, FileDto.class);
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
}
