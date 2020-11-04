package com.eboy.honey.oss.server.api.rpc.dubbo.impl;


import com.eboy.honey.oss.api.constant.FileState;
import com.eboy.honey.oss.api.dto.FileDto;
import com.eboy.honey.oss.api.dto.HoneyStream;
import com.eboy.honey.oss.api.service.FileRpcService;
import com.eboy.honey.oss.api.utils.HoneyFileUtil;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.utils.BeanConvertUtil;
import com.eboy.honey.oss.server.application.vo.FileVo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/8/6 13:38
 */
@Service(version = "1.0")
public class FileRpcServiceImpl implements FileRpcService {

    @Autowired
    private FileService fileService;


    /**
     * 上传文件(不分片)
     *
     * @param file        文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @return fileKey
     */
    @Override
    public String upload(File file, String bucketName, MediaType contentType) {
        return fileService.upload(file, bucketName, contentType);
    }

    /**
     * 文件上传(不分片)
     *
     * @param fileDto     文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @return fileKey
     */
    @Override
    public String upload(FileDto fileDto, String bucketName, MediaType contentType) {
        FileVo fileVo = BeanConvertUtil.convert(fileDto, FileVo.class);
        return fileService.upload(fileVo, bucketName, contentType);
    }

    /**
     * 分片上传
     *
     * @param fileDto     文件
     * @param bucketName  桶名
     * @param contentType contentType
     * @return fileKey
     */
    @Override
    public String uploadByShard(FileDto fileDto, String bucketName, MediaType contentType) {
        FileVo fileVo = BeanConvertUtil.convert(fileDto, FileVo.class);
        return fileService.uploadByShard(fileVo, bucketName, contentType);
    }

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
     * 下载至本地
     *
     * @param bucketName   桶名
     * @param fileKey      fileKey
     * @param fileDownPath 指定下载到本地的文件目录
     */
    @Override
    public void down2Local(String bucketName, String fileKey, String fileDownPath) {
        InputStream inputStream = downAsStream(bucketName, fileKey).getInputStream();
        HoneyFileUtil.writeToLocal(fileDownPath, inputStream);
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
        return BeanConvertUtil.convertByList(files, FileDto.class);
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

    /**
     * 上传图片
     *
     * @param image         图片源
     * @param bucketName    桶名
     * @param contentType   contentType
     * @param needThumbnail 是否需要缩略图
     * @return 原图fileKey
     */
    @Override
    public String uploadImage(File image, String bucketName, MediaType contentType, boolean needThumbnail) {
        return fileService.uploadImage(image, bucketName, contentType, needThumbnail);
    }
}
