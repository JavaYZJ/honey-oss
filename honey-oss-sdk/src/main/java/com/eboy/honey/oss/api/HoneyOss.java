package com.eboy.honey.oss.api;

import com.eboy.honey.oss.api.constant.FileState;
import com.eboy.honey.oss.api.dto.FileDto;
import com.eboy.honey.oss.api.dto.FileShardDto;
import com.eboy.honey.oss.api.entiy.CallBack;
import com.eboy.honey.oss.api.entiy.Response;
import com.eboy.honey.oss.api.entiy.Thumbnail;
import com.eboy.honey.oss.api.service.dubbo.FileRpcService;
import com.eboy.honey.oss.api.service.dubbo.FileShardRpcService;
import com.eboy.honey.oss.api.service.dubbo.PureFileRpcService;
import com.eboy.honey.oss.api.service.dubbo.ThumbnailRpcService;
import com.eboy.honey.oss.api.utils.HoneyFileUtil;
import com.eboy.honey.oss.client.HoneyMiniO;
import com.eboy.honey.oss.utils.BeanConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author yangzhijie
 * @date 2020/11/4 10:41
 */
@Slf4j
public class HoneyOss {

    @Reference(version = "1.0")
    private FileRpcService fileRpcService;

    @Reference(version = "1.0")
    private FileShardRpcService fileShardRpcService;

    @Reference(version = "1.0")
    private ThumbnailRpcService thumbnailRpcService;

    @Reference(version = "1.0")
    private PureFileRpcService postFileRpcService;

    @Autowired
    private HoneyMiniO honeyMiniO;


    /**
     * 上传文件
     * <p>
     * 默认会自动分片上传，当单个文件小于5M则不会触发分片上传机制
     * 支持单个最大文件为5T
     * 但是这种情况的分片不会保存在honey-oss上
     *
     * @param file        文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @return fileKey
     */
    public String upload(File file, String bucketName, MediaType contentType) {
        // file convert to fileDto
        FileDto fileDto = BeanConverter.convert2FileDto(file);
        fileDto.setBucketName(bucketName);
        fileDto.setFileState(FileState.SUCCESS.getStateCode());
        return upload(fileDto, bucketName, contentType);
    }


    public Response<FileShardDto> uploadByShard(String filePath, int count) {
        File file = new File(filePath);
        FileDto fileDto = HoneyFileUtil.convertFileDto(file);
        HoneyFileUtil.spiltFile(filePath, count);
        for (int i = 1; i <= count; i++) {
            FileShardDto fileShardDto = new FileShardDto();
            fileShardDto.setUid(HoneyFileUtil.get32Uid());
            fileShardDto.setFileKey(fileDto.getFileKey());

        }
        return null;
    }

    /**
     * 分片上传
     *
     * @param fileDto     文件分片
     * @param bucketName  桶名
     * @param contentType contentType
     * @return fileKey
     */
    private Response<FileShardDto> uploadByShard(FileDto fileDto, String bucketName, MediaType contentType) throws InterruptedException {
        // 该文件是不是第一次分片上传
        boolean isExit = fileRpcService.getFileByFileKeys(Collections.singletonList(fileDto.getFileKey())).size() > 0;
        if (!isExit) {
            // 插入到数据库
            postFileRpcService.postFileInfo(fileDto);
        }
        List<FileShardDto> fileShardDtos = fileDto.getFileShardDtos();
        Assert.notEmpty(fileShardDtos, "File shards must not empty");
        return shardProcess(fileDto, bucketName, contentType, fileShardDtos);
    }

    private Response<FileShardDto> shardProcess(FileDto fileDto, String bucketName, MediaType contentType, List<FileShardDto> fileShardDtos) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(fileShardDtos.size());
        List<FileShardDto> success = Collections.synchronizedList(new ArrayList<>());
        List<FileShardDto> failures = Collections.synchronizedList(new ArrayList<>());
        fileShardDtos.parallelStream().forEach(fileShardDto -> {
            try {
                shardUpload(bucketName, contentType, fileShardDto);
                success.add(fileShardDto);
            } catch (Exception e) {
                failures.add(fileShardDto);
            } finally {
                latch.countDown();
            }
        });
        latch.await();
        return responseWarp(fileDto, fileShardDtos, success, failures);
    }

    private Response<FileShardDto> responseWarp(FileDto fileDto, List<FileShardDto> fileShardDtos, List<FileShardDto> success, List<FileShardDto> failures) {
        Response<FileShardDto> response = new Response<>();
        response.setSuccess(success);
        response.setFailures(failures);
        if (success.size() == fileShardDtos.size()) {
            response.setFileKey(fileDto.getFileKey());
        }
        return response;
    }

    private void shardUpload(String bucketName, MediaType contentType, FileShardDto fileShardDto) {
        // 上传至MiniO
        String objectName = HoneyFileUtil.buildObjectNameByFileKey(fileShardDto.getShardName(), fileShardDto.getFileKey());
        honeyMiniO.upload(bucketName, objectName, fileShardDto.getHoneyStream().getInputStream(), contentType);
        // 上传分片到数据库
        fileShardDto.setShardState(FileState.SUCCESS.getStateCode());
        fileShardRpcService.addFileShard(fileShardDto);
    }

    /**
     * 上传文件(私有)
     * <p>
     * 默认会自动分片上传，当单个文件小于5M则不会触发分片上传机制
     * 支持单个最大文件为5T
     * 但是这种情况的分片不会保存在honey-oss上
     *
     * @param fileDto     文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @return fileKey
     */
    private String upload(FileDto fileDto, String bucketName, MediaType contentType) {
        postFileRpcService.postFileInfo(fileDto);
        // upload to MiniO
        String objectName = HoneyFileUtil.buildObjectNameByFileKey(fileDto.getFileName(), fileDto.getFileKey());
        honeyMiniO.upload(bucketName, objectName, fileDto.getHoneyStream().getInputStream(), contentType);
        return fileDto.getFileKey();
    }

    /**
     * 异步文件上传
     *
     * @param file        文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @param callBack    回调参数
     */
    public void asyncUpload(File file, String bucketName, MediaType contentType, CallBack callBack) {
    }

    /**
     * 下载为url
     *
     * @param bucketName 桶名
     * @param fileKey    对象名
     * @return string 文件的url
     */
    public String downAsUrl(String bucketName, String fileKey) {
        return fileRpcService.downAsUrl(bucketName, fileKey);
    }

    /**
     * 下载为url
     *
     * @param bucketName 桶名
     * @param fileKey    文件fileKey
     * @param expires    过期时间(秒)
     * @return string 文件的url
     */
    public String downAsUrl(String bucketName, String fileKey, Integer expires) {
        return fileRpcService.downAsUrl(bucketName, fileKey, expires);
    }

    /**
     * 下载为文件流
     *
     * @param bucketName 桶名
     * @param fileKey    fileKey
     * @return InputStream 文件流
     */
    public InputStream downAsStream(String bucketName, String fileKey) {
        String objectName = objectName(fileKey, bucketName);
        return honeyMiniO.downAsStream(bucketName, objectName);
    }

    /**
     * 下载至本地
     *
     * @param bucketName   桶名
     * @param fileKey      fileKey
     * @param fileDownPath 指定下载到本地的文件目录
     */
    public void down2Local(String bucketName, String fileKey, String fileDownPath) {
        String objectName = objectName(fileKey, bucketName);
        honeyMiniO.down2Local(bucketName, objectName, fileDownPath);
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
    public String uploadImage(File image, String bucketName, MediaType contentType, boolean needThumbnail) {
        // 先上传原图
        String fileKey = upload(image, bucketName, contentType);
        if (needThumbnail) {
            Thumbnail defaultThumbnail = thumbnailRpcService.defaultThumbnail(image);
            thumbnailHandle(bucketName, contentType, fileKey, defaultThumbnail);
        }
        return fileKey;
    }

    /**
     * 自定义缩略图规则上传图片
     *
     * @param image       图片源
     * @param bucketName  桶名
     * @param contentType contentType
     * @param thumbnail   缩略图规则
     * @return 原图fileKey
     */
    public String uploadImage(File image, String bucketName, MediaType contentType, Thumbnail thumbnail) {
        // 先上传原图
        String fileKey = upload(image, bucketName, contentType);
        thumbnail = thumbnailRpcService.buildThumbnail(thumbnail);
        thumbnailHandle(bucketName, contentType, fileKey, thumbnail);
        return fileKey;
    }

    /**
     * 根据原图获取缩略图链接
     *
     * @param fileKey    原图fileKey
     * @param bucketName 桶名
     * @return 缩略图链接
     */
    public String getUrlByOriginalPicture(String bucketName, String fileKey) {
        String thumbnailFileKey = HoneyFileUtil.getThumbnailFileKey(fileKey);
        return downAsUrl(bucketName, thumbnailFileKey);
    }


    /**
     * 根据原图获取缩略图链接
     *
     * @param fileKey    原图fileKey
     * @param bucketName 桶名
     * @param expires    过期时间（秒）
     * @return 缩略图链接
     */
    public String getUrlByOriginalPicture(String bucketName, String fileKey, Integer expires) {
        String thumbnailFileKey = HoneyFileUtil.getThumbnailFileKey(fileKey);
        return downAsUrl(bucketName, thumbnailFileKey, expires);
    }

    /**
     * 根据原图将缩略图下载至指定本地路径
     *
     * @param bucketName   桶名
     * @param fileKey      原图fileKey
     * @param fileDownPath 指定本地路径
     */
    public void down2LocalByOriginalPicture(String bucketName, String fileKey, String fileDownPath) {
        String thumbnailFileKey = HoneyFileUtil.getThumbnailFileKey(fileKey);
        down2Local(bucketName, thumbnailFileKey, fileDownPath);
    }

    /**
     * 根据ids查找文件
     *
     * @param ids 文件ids
     * @return 文件
     */
    public List<FileDto> getFileByIds(List<String> ids) {
        return fileRpcService.getFileByIds(ids);
    }

    /**
     * 根据fileKey查找文件
     *
     * @param fileKeys 文件keys
     * @return 文件
     */
    public List<FileDto> getFileByFileKeys(List<String> fileKeys) {
        return fileRpcService.getFileByFileKeys(fileKeys);
    }

    /**
     * 更新文件名
     *
     * @param fileId   文件id
     * @param fileName 文件名
     * @return 是否成功
     */
    public boolean updateFileName(String fileId, String fileName) {
        return fileRpcService.updateFileName(fileId, fileName);
    }

    /**
     * 根据ids删除文件
     *
     * @param ids 文件ids
     * @return 是否删除成功
     */
    public boolean deleteFileByIds(List<String> ids) {
        return fileRpcService.deleteFileByIds(ids);
    }

    /**
     * 根据fileKeys删除文件
     *
     * @param fileKeys 文件keys
     * @return 是否成功
     */
    public boolean deletedFileByFileKeys(List<String> fileKeys) {
        return fileRpcService.deletedFileByFileKeys(fileKeys);
    }

    /**
     * 通过fileKey构建objectName
     *
     * @param fileKey 文件fileKey
     * @return objectName
     */
    private String objectName(String fileKey, String bucketName) {
        String fileName = fileRpcService.getFileByFileKeys(Collections.singletonList(fileKey))
                .stream()
                .filter(e -> e.getBucketName().equals(bucketName))
                .map(FileDto::getFileName)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("fileKey not found"));
        return HoneyFileUtil.buildObjectNameByFileKey(fileName, fileKey);
    }

    /**
     * 缩略图上传前组装
     *
     * @param bucketName       桶名
     * @param contentType      contentType
     * @param fileKey          原图的fileKey
     * @param defaultThumbnail 缩略图构建规则
     */
    private void thumbnailHandle(String bucketName, MediaType contentType, String fileKey, Thumbnail defaultThumbnail) {
        String filePath = defaultThumbnail.getOutputMode().getFilePath();
        File thumbnail = new File(filePath);
        FileDto fileDto = BeanConverter.convert2FileDto(thumbnail);
        String thumbnailFileKey = HoneyFileUtil.getThumbnailFileKey(fileKey);
        fileDto.setFileKey(thumbnailFileKey);
        fileDto.setBucketName(bucketName);
        // 上传缩略图
        upload(fileDto, bucketName, contentType);
    }
}
