package com.eboy.honey.oss.server.application.service.impl;

import com.eboy.honey.oss.constant.FileState;
import com.eboy.honey.oss.dto.FileUpload;
import com.eboy.honey.oss.server.application.componet.AsyncTask;
import com.eboy.honey.oss.server.application.dao.FileMapper;
import com.eboy.honey.oss.server.application.po.FilePo;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.service.FileShardService;
import com.eboy.honey.oss.server.application.utils.BeanConvertUtil;
import com.eboy.honey.oss.server.application.vo.FileShardVo;
import com.eboy.honey.oss.server.application.vo.FileVo;
import com.eboy.honey.oss.server.client.HoneyMiniO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${honey.oss.minio.bucketName}")
    private String bucketName;

    @Autowired
    private HoneyMiniO honeyMiniO;

    @Autowired
    private FileShardService fileShardService;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private AsyncTask asyncTask;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean uploadFile(FileUpload fileUpload, String bucketName, ContentType contentType) {
        //

        // 上传前检查一下服务里是否有其他用户已经上传过该文件，如果有，则实现秒传
        if (secondTransCheck(fileUpload)) {
            return true;
        }
        File file = fileVo.getFile();
        if (file != null) {
            try {
                // 上传的是整个文件，不做分片处理
                FileInputStream inputStream = new FileInputStream(file);
                honeyMiniO.upload(bucketName, fileVo.getFileKey(), inputStream, contentType);
                // 上传完毕，，添加信息到数据库
                FilePo filePo = BeanConvertUtil.convert(fileVo, FilePo.class);
                filePo.setFileState(FileState.SUCCESS.getStateCode());
                fileMapper.addFile(filePo);
            } catch (FileNotFoundException e) {
                log.warn("上传整个文件失败，原因：{}", e.getMessage());
                throw new RuntimeException("上传整个文件失败");
            }
        } else {
            // 分块上传
            List<FileShardVo> fileShardVos = fileVo.getFileShardVos();
            Assert.isTrue(!CollectionUtils.isEmpty(fileShardVos), "请传入file或者分片的file实例");
            fileShardVos
            // 再逐个上传分片(1、上传到Minio 2、插入到数据库)
            honeyMiniO.upload(bucketName, fileShardVo.getShardName(), fileShardVo.getFileShardStream(), contentType);
            fileShardVo.setShardState(FileState.SUCCESS.getStateCode());
            fileShardService.addFileShard(fileShardVo);
            // 异步检查是否最后一块并修改文件状态
            asyncTask.asyncCheckAndMerge(fileShardVo);
        }
        return true;
    }

    /**
     * 异步上传文件
     *
     * @param fileVo      文件实体
     * @param fileShardVo 分片
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 是否上传成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean asyncUploadFile(FileVo fileVo, FileShardVo fileShardVo,
                                   String bucketName, ContentType contentType) {
        // 上传前检查一下服务里是否有其他用户已经上传过该文件，如果有，则实现秒传
        if (secondTransCheck(fileVo)) {
            return true;
        }
        // 创建文件信息
        FilePo filePo = BeanConvertUtil.convert(fileVo, FilePo.class);
        fileMapper.addFile(filePo);
        // 插入到数据库
        fileShardService.addFileShard(fileShardVo);
        // 异步上传到Minio
        asyncTask.asyncUpload(fileShardVo, bucketName, contentType);
        return true;
    }

    /**
     * 文件上传
     *
     * @param fileVo      文件实体
     * @param inputStream 文件流
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 是否成功
     */
    @Override
    public boolean upload(FileVo fileVo, InputStream inputStream, String bucketName, ContentType contentType) {
        // 上传前检查一下服务里是否有其他用户已经上传过该文件，如果有，则实现秒传
        if (secondTransCheck(fileVo)) {
            return true;
        }
        // 创建文件信息
        FilePo filePo = BeanConvertUtil.convert(fileVo, FilePo.class);
        honeyMiniO.upload(bucketName, fileVo.getFileKey(), inputStream, contentType);
        return fileMapper.addFile(filePo);
    }

    public boolean upload(FileVo fileVo, InputStream inputStream, ContentType contentType) {
        return upload(fileVo, inputStream, bucketName, contentType);
    }


    public boolean uploadFile(FileVo fileVo, FileShardVo fileShardVo, ContentType contentType) {
        return uploadFile(fileVo, fileShardVo, bucketName, contentType);
    }

    /**
     * 异步上传文件
     *
     * @param fileVo      文件实体
     * @param fileShardVo 分片
     * @param contentType contentType
     * @return 是否上传成功
     */
    public boolean asyncUploadFile(FileVo fileVo, FileShardVo fileShardVo, ContentType contentType) {
        return asyncUploadFile(fileVo, fileShardVo, bucketName, contentType);
    }

    /**
     * 下载为url
     *
     * @param objectName 对象名
     * @return string 文件的url
     */
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
     * 更新文件状态
     *
     * @param fileKey   文件key
     * @param fileState 文件状态
     * @return 是否成功
     */
    @Override
    public boolean updateFileState(String fileKey, FileState fileState) {
        return fileMapper.updateFileState(fileKey, fileState.getStateCode());
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
        return fileMapper.getFileByFileKeys(
                Collections.singletonList(fileVo.getFileKey()))
                .stream()
                .anyMatch(e -> e.getFileState() == FileState.SUCCESS.getStateCode()
                );
    }

    private FileVo buildFileVo(FileUpload fileUpload) {
        File file = fileUpload.getFile();
        Asserts.notNull(file, "file");
        FileVo fileVo = new FileVo();
        String fileName = file.getName();
        fileVo.setFileName(fileName);
        return fileVo;
    }

}
