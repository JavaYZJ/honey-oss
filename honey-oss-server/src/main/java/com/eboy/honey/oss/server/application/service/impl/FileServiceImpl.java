package com.eboy.honey.oss.server.application.service.impl;

import com.eboy.honey.oss.constant.FileState;
import com.eboy.honey.oss.server.application.BeanConvertUtil;
import com.eboy.honey.oss.server.application.dao.FileMapper;
import com.eboy.honey.oss.server.application.po.FilePo;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.service.FileShardService;
import com.eboy.honey.oss.server.application.vo.FileShardVo;
import com.eboy.honey.oss.server.application.vo.FileVo;
import com.eboy.honey.oss.server.client.HoneyMiniO;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yangzhijie
 * @date 2020/7/29 16:57
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private HoneyMiniO honeyMiniO;

    @Autowired
    private FileShardService fileShardService;

    @Autowired
    private FileMapper fileMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean uploadFile(FileVo fileVo, FileShardVo fileShardVo, String bucketName,ContentType contentType) {
        // 上传前检查一下服务里是否有其他用户已经上传过该文件，如果有，则实现秒传
        if (secondTransCheck(fileVo)) {
            return true;
        }
        // 创建文件信息
        FilePo filePo = BeanConvertUtil.convert(fileVo, FilePo.class);
        fileMapper.addFile(filePo);
        // 再逐个上传分片(1、上传到Minio 2、插入到数据库)
        honeyMiniO.upload(bucketName, fileShardVo.getShardName(), fileShardVo.getFileShardStream(), contentType);
        return fileShardService.addFileShard(fileShardVo);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean uploadFile(FileVo fileVo, FileShardVo fileShardVo,ContentType contentType) {
        // 上传前检查一下服务里是否有其他用户已经上传过该文件，如果有，则实现秒传
        if (secondTransCheck(fileVo)) {
            return true;
        }
        // 创建文件信息
        FilePo filePo = BeanConvertUtil.convert(fileVo, FilePo.class);
        fileMapper.addFile(filePo);
        // 再逐个上传分片(1、上传到Minio 2、插入到数据库)
        honeyMiniO.upload(fileShardVo.getShardName(), fileShardVo.getFileShardStream(), contentType);
        return fileShardService.addFileShard(fileShardVo);

    }

    /**
     * 下载为url
     *
     * @param objectName 对象名
     * @return string 文件的url
     */
    @Override
    public String downAsUrl(String objectName) {
        return honeyMiniO.downAsUrl(objectName);
    }

    /**
     * 下载为url
     *
     * @param bucketName 桶名
     * @param objectName 对象名
     * @return string 文件的url
     */
    @Override
    public String downAsUrl(String bucketName, String objectName) {
        return honeyMiniO.downAsUrl(bucketName, objectName);
    }

    /**
     * 下载为文件流
     *
     * @param bucketName 桶名
     * @param objectName 对象名
     * @return InputStream 文件流
     */
    @Override
    public InputStream downAsStream(String bucketName, String objectName) {
        return honeyMiniO.downAsStream(bucketName, objectName);
    }

    /**
     * 下载为文件流
     *
     * @param objectName 对象名
     * @return InputStream 文件流
     */
    @Override
    public InputStream downAsStream(String objectName) {
        return honeyMiniO.downAsStream(objectName);
    }


    @Override
    public List<FileVo> getFileByIds(List<String> ids) {
        List<FilePo> filePos = fileMapper.getFileByIds(ids);
        return mergeFileShad(filePos);
    }

    @Override
    public List<FileVo> getFileByFileKeys(List<String> fileKeys) {
        List<FilePo> filePos = fileMapper.getFileByFileKeys(fileKeys);
        return mergeFileShad(filePos);
    }

    @Override
    public boolean updateFileName(String fileId, String fileName) {
        return fileMapper.updateFileName(fileId, fileName);
    }

    @Override
    public boolean deleteFileByIds(List<String> ids) {
        return fileMapper.deleteFileByIds(ids);
    }

    @Override
    public boolean deletedFileByFileKeys(List<String> fileKeys) {
        return fileMapper.deletedFileByFileKeys(fileKeys);
    }

    /**
     * 将分片信息拼入文件并进行转化
     *
     * @param sources 文件源
     * @return 含有分片信息的文件
     */
    private List<FileVo> mergeFileShad(List<FilePo> sources) {
        Map<String, List<FileShardVo>> rs = fileShardService.getFileShardInfoByFileKeys(
                sources.stream()
                        .map(FilePo::getFileKey)
                        .collect(Collectors.toList()));
        List<FileVo> fileVos = BeanConvertUtil.convertByList(sources, FileVo.class);
        fileVos.forEach(fileVo -> fileVo.setFileShardVos(rs.get(fileVo.getFileKey())));
        return fileVos;
    }


    /**
     * 是否秒传检查
     */
    private boolean secondTransCheck(FileVo fileVo) {
        return fileMapper.getFileByFileKeys(Collections.singletonList(fileVo.getFileKey())).stream().anyMatch(e -> e.getFileState() == FileState.SUCCESS.getStateCode());
    }

}
