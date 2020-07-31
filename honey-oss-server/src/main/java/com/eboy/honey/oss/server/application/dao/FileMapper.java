package com.eboy.honey.oss.server.application.dao;


import com.eboy.honey.oss.server.application.po.FilePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/7/29 15:44
 */
@Mapper
public interface FileMapper {

    /**
     * 新增文件
     *
     * @param filePo 文件实体
     * @return 是否成功
     */
    boolean addFile(@Param("file") FilePo filePo);

    /**
     * 根据ids查找文件
     *
     * @param ids 文件ids
     * @return 文件
     */
    List<FilePo> getFileByIds(@Param("ids") List<String> ids);

    /**
     * 根据fileKey查找文件
     *
     * @param fileKeys 文件keys
     * @return 文件
     */
    List<FilePo> getFileByFileKeys(@Param("fileKeys") List<String> fileKeys);

    /**
     * 更新文件名
     *
     * @param fileId   文件id
     * @param fileName 文件名
     * @return 是否成功
     */
    boolean updateFileName(@Param("fileId") String fileId, @Param("fileName") String fileName);

    /**
     * 更新文件状态
     *
     * @param fileId 文件id
     * @param fileState 文件状态
     * @return 是否成功
     */
    boolean updateFileState(@Param("fileId") String fileId, @Param("fileState") int fileState);

    /**
     * 根据ids删除文件
     *
     * @param ids 文件ids
     * @return 是否删除成功
     */
    boolean deleteFileByIds(@Param("ids") List<String> ids);

    /**
     * 根据fileKeys删除文件
     *
     * @param fileKeys 文件keys
     * @return 是否成功
     */
    boolean deletedFileByFileKeys(@Param("fileKeys") List<String> fileKeys);
}
