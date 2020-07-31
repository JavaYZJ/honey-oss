package com.eboy.honey.oss.server.application.service;



import com.eboy.honey.oss.server.api.dto.FileDto;
import com.eboy.honey.oss.server.application.po.FilePo;
import com.eboy.honey.oss.server.application.vo.FileVo;

import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/7/29 16:49
 */
public interface FileService {

    /**
     * 上传文件
     *
     * @param fileVo 文件实体
     * @return 是否上传成功
     */
    boolean addLocalFile(FileVo fileVo);

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
