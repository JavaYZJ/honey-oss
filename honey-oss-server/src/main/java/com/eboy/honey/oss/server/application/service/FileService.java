package com.eboy.honey.oss.server.application.service;


import com.eboy.honey.oss.constant.FileState;
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
     * @return 是否上传成功
     */
    boolean upload(File file, String bucketName, MediaType contentType);

    /**
     * 异步上传文件（不分片）
     *
     * @param file        文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 是否上传成功
     */
    boolean asyncUpload(File file, String bucketName, MediaType contentType);

    /**
     * 文件上传(不分片)
     *
     * @param fileVo      文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 是否成功
     */
    boolean upload(FileVo fileVo, String bucketName, MediaType contentType);

    /**
     * 异步文件上传(不分片)
     *
     * @param fileVo      文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 是否成功
     */
    boolean asyncUpload(FileVo fileVo, String bucketName, MediaType contentType);


    /**
     * 分片上传
     *
     * @param fileVo      文件
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 是否成功
     */
    boolean uploadByShard(FileVo fileVo, String bucketName, MediaType contentType);

    /**
     * 异步分片上传
     *
     * @param fileVo      文件
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 是否成功
     */
    boolean asyncUploadByShard(FileVo fileVo, String bucketName, MediaType contentType);

    /**
     * 下载为url
     *
     * @param bucketName 桶名
     * @param fileKey    对象名
     * @return string 文件的url
     */
    String downAsUrl(String bucketName, String fileKey);

    /**
     * 下载为文件流
     *
     * @param bucketName 桶名
     * @param fileKey    对象名
     * @return InputStream 文件流
     */
    InputStream downAsStream(String bucketName, String fileKey);

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
}
