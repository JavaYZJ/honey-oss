package com.eboy.honey.oss.server.application.service;


import com.eboy.honey.oss.server.application.vo.FileShardVo;
import com.eboy.honey.oss.server.application.vo.FileVo;
import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/7/29 16:49
 */
public interface FileService {


    /**
     * 上传文件
     *
     * @param fileVo      文件实体
     * @param fileShardVo 分片
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 是否上传成功
     */
    boolean uploadFile(FileVo fileVo, FileShardVo fileShardVo, String bucketName, ContentType contentType);

    /**
     * 异步上传文件
     *
     * @param fileVo      文件实体
     * @param fileShardVo 分片
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 是否上传成功
     */
    boolean asyncUploadFile(FileVo fileVo, FileShardVo fileShardVo, String bucketName, ContentType contentType);

    /**
     * 上传文件
     *
     * @param fileVo      文件实体
     * @param fileShardVo 分片
     * @param contentType contentType
     * @return 是否上传成功
     */
    boolean uploadFile(FileVo fileVo, FileShardVo fileShardVo, ContentType contentType);

    /**
     * 上传文件
     *
     * @param fileVo      文件实体
     * @param fileShardVo 分片
     * @param contentType contentType
     * @return 是否上传成功
     */
    boolean asyncUploadFile(FileVo fileVo, FileShardVo fileShardVo, ContentType contentType);

    /**
     * 下载为url
     *
     * @param objectName 对象名
     * @return string 文件的url
     */
    String downAsUrl(String objectName);

    /**
     * 下载为url
     *
     * @param bucketName 桶名
     * @param objectName 对象名
     * @return string 文件的url
     */
    String downAsUrl(String bucketName, String objectName);

    /**
     * 下载为文件流
     *
     * @param bucketName 桶名
     * @param objectName 对象名
     * @return InputStream 文件流
     */
    InputStream downAsStream(String bucketName, String objectName);

    /**
     * 下载为文件流
     *
     * @param objectName 对象名
     * @return InputStream 文件流
     */
    InputStream downAsStream(String objectName);


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
}
