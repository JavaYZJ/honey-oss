package com.eboy.honey.oss.dubbo.service;


import com.eboy.honey.oss.dto.FileDto;

import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/8/6 13:34
 */
public interface FileRpcService {

    /**
     * 上传文件
     *
     * @param fileDto 文件实体
     * @return 是否上传成功
     */
    boolean addLocalFile(FileDto fileDto);

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
