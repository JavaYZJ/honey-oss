package com.eboy.honey.oss.dubbo;


import com.eboy.honey.oss.constant.FileState;
import com.eboy.honey.oss.dto.FileDto;
import com.eboy.honey.oss.dto.HoneyStream;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/8/6 13:34
 */
public interface FileRpcService {


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
     * 文件上传(不分片)
     *
     * @param fileDto     文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @return fileKey
     */
    String upload(FileDto fileDto, String bucketName, MediaType contentType);


    /**
     * 分片上传
     *
     * @param fileDto     文件
     * @param bucketName  桶名
     * @param contentType contentType
     * @return fileKey
     */
    String uploadByShard(FileDto fileDto, String bucketName, MediaType contentType);


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
    HoneyStream downAsStream(String bucketName, String fileKey);

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
    List<FileDto> getFileByIds(List<String> ids);

    /**
     * 根据fileKey查找文件
     *
     * @param fileKeys 文件keys
     * @return 文件
     */
    List<FileDto> getFileByFileKeys(List<String> fileKeys);

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
     * @return 是否成功
     */
    String uploadImage(File image, String bucketName, MediaType contentType, boolean needThumbnail);


}
