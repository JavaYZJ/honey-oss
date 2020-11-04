package com.eboy.honey.oss.server.application.service;


import com.eboy.honey.oss.api.constant.FileState;
import com.eboy.honey.oss.api.dto.HoneyStream;
import com.eboy.honey.oss.api.entiy.Thumbnail;
import com.eboy.honey.oss.server.application.vo.FileVo;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/7/29 16:49
 */
public interface FileService {


    /**
     * 上传文件(不分片)
     *
     * @param file        文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @return fileKey
     */
    String upload(File file, String bucketName, MediaType contentType);

    /**
     * 异步上传文件（不分片）
     *
     * @param file        文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @param callbackUrl 回调url
     */
    void asyncUpload(File file, String bucketName, MediaType contentType, String callbackUrl);

    /**
     * 文件上传(不分片)
     *
     * @param fileVo      文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @return fileKey
     */
    String upload(FileVo fileVo, String bucketName, MediaType contentType);

    /**
     * 异步文件上传(不分片)
     *
     * @param fileVo      文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @param callbackUrl 回调url
     */
    void asyncUpload(FileVo fileVo, String bucketName, MediaType contentType, String callbackUrl);


    /**
     * 分片上传
     *
     * @param fileVo      文件
     * @param bucketName  桶名
     * @param contentType contentType
     * @return fileKey
     */
    String uploadByShard(FileVo fileVo, String bucketName, MediaType contentType);

    /**
     * 异步分片上传
     *
     * @param fileVo      文件
     * @param bucketName  桶名
     * @param contentType contentType
     * @param callbackUrl 回调url
     */
    void asyncUploadByShard(FileVo fileVo, String bucketName, MediaType contentType, String callbackUrl);

    /**
     * 下载为url
     *
     * @param bucketName 桶名
     * @param fileKey    对象名
     * @return string 文件的url
     */
    String downAsUrl(String bucketName, String fileKey);

    /**
     * 下载为url
     *
     * @param bucketName 桶名
     * @param fileKey    文件fileKey
     * @param expires    过期时间(秒)
     * @return string 文件的url
     */
    String downAsUrl(String bucketName, String fileKey, Integer expires);

    /**
     * 下载为文件流
     *
     * @param bucketName 桶名
     * @param fileKey    fileKey
     * @return InputStream 文件流
     */
    InputStream downAsStream(String bucketName, String fileKey);


    /**
     * 下载为文件流
     *
     * @param bucketName 桶名
     * @param fileKey    fileKey
     * @return HoneyStream 文件流
     */
    HoneyStream downAsHoneyStream(String bucketName, String fileKey);

    /**
     * 下载至本地
     *
     * @param bucketName   桶名
     * @param fileKey      fileKey
     * @param fileDownPath 指定下载到本地的文件目录
     */
    void down2Local(String bucketName, String fileKey, String fileDownPath);

    /**
     * 根据ids查找文件
     *
     * @param ids 文件ids
     * @return 文件
     */
    List<FileVo> getFileByIds(List<String> ids);

    /**
     * 根据fileKey查找文件
     *
     * @param fileKeys 文件keys
     * @return 文件
     */
    List<FileVo> getFileByFileKeys(List<String> fileKeys);

    /**
     * 更新文件名
     *
     * @param fileId   文件id
     * @param fileName 文件名
     * @return 是否成功
     */
    boolean updateFileName(String fileId, String fileName);

    /**
     * 根据ids删除文件
     *
     * @param ids 文件ids
     * @return 是否删除成功
     */
    boolean deleteFileByIds(List<String> ids);

    /**
     * 根据fileKeys删除文件
     *
     * @param fileKeys 文件keys
     * @return 是否成功
     */
    boolean deletedFileByFileKeys(List<String> fileKeys);

    /**
     * 更新文件状态
     *
     * @param fileKey   文件key
     * @param fileState 文件状态
     * @return 是否成功
     */
    boolean updateFileState(String fileKey, FileState fileState);

    /**
     * 上传图片
     *
     * @param image         图片源
     * @param bucketName    桶名
     * @param contentType   contentType
     * @param needThumbnail 是否需要缩略图
     * @return 原图fileKey
     */
    String uploadImage(File image, String bucketName, MediaType contentType, boolean needThumbnail);

    /**
     * 自定义缩略图规则上传图片
     *
     * @param image       图片源
     * @param bucketName  桶名
     * @param contentType contentType
     * @param thumbnail   缩略图规则
     * @return 原图fileKey
     */
    String uploadImage(File image, String bucketName, MediaType contentType, Thumbnail thumbnail);

}
