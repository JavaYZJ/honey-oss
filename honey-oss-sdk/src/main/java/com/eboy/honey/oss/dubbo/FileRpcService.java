package com.eboy.honey.oss.dubbo;


import com.eboy.honey.oss.dto.FileDto;
import com.eboy.honey.oss.dto.FileShardDto;
import org.apache.http.entity.ContentType;

import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/8/6 13:34
 */
public interface FileRpcService {

    /**
     * 上传文件
     *
     * @param fileDto      文件实体
     * @param fileShardDto 分片信息
     * @param bucketName   桶名
     * @param contentType  contentType
     * @return 是否上传成功
     */
    boolean uploadFileShard(FileDto fileDto, FileShardDto fileShardDto, String bucketName, ContentType contentType);

    /**
     * 上传文件
     *
     * @param fileDto     文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 是否成功
     */
    boolean uploadFile(FileDto fileDto, String bucketName, ContentType contentType);

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
}
