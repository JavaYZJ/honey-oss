package red.honey.oss.api.service.dubbo;


import red.honey.oss.api.constant.FileState;
import red.honey.oss.api.dto.FileDto;
import red.honey.oss.api.dto.HoneyStream;

import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/8/6 13:34
 */
public interface FileRpcService {

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


}
