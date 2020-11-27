package red.honey.oss.server.application.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import red.honey.oss.api.constant.FileState;
import red.honey.oss.api.dto.HoneyStream;
import red.honey.oss.api.entiy.CallBack;
import red.honey.oss.api.entiy.Thumbnail;
import red.honey.oss.api.utils.CallBackUtil;
import red.honey.oss.api.utils.HoneyFileUtil;
import red.honey.oss.server.application.componet.AsyncTask;
import red.honey.oss.server.application.dao.FileMapper;
import red.honey.oss.server.application.factory.CallbackFactory;
import red.honey.oss.server.application.po.FilePo;
import red.honey.oss.server.application.service.FileService;
import red.honey.oss.server.application.service.FileShardService;
import red.honey.oss.server.application.service.ThumbnailService;
import red.honey.oss.server.application.utils.ArgsCheckUtil;
import red.honey.oss.server.application.utils.BeanConvertUtil;
import red.honey.oss.server.application.vo.FileShardVo;
import red.honey.oss.server.application.vo.FileVo;
import red.honey.oss.server.client.HoneyMiniO;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static red.honey.oss.api.constant.CallbackEnum.getByCode;


/**
 * @author yangzhijie
 * @date 2020/7/29 16:57
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${honey.oss.callback-type}")
    private int callbackType;

    @Autowired
    private HoneyMiniO honeyMiniO;
    @Autowired
    private FileShardService fileShardService;
    @Autowired
    private ThumbnailService thumbnailService;
    @Autowired
    private FileMapper fileMapper;
    @Lazy
    @Autowired
    private AsyncTask asyncTask;


    /**
     * 直接单个上传（不分片）
     *
     * @param fileVo      文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String upload(FileVo fileVo, String bucketName, MediaType contentType) {
        // 参数校验
        ArgsCheckUtil.checkFile(fileVo, false);
        // 上传前检查一下服务里是否有其他用户已经上传过该文件，如果有，则实现秒传
        if (secondTransCheck(fileVo, bucketName)) {
            log.info("秒传");
            return fileVo.getFileKey();
        }
        // 创建文件信息
        FilePo filePo = BeanConvertUtil.convert(fileVo, FilePo.class);
        // 插入到数据库
        filePo.setFileState(FileState.SUCCESS.getStateCode());
        fileMapper.addFile(filePo);
        // 上传到MiniO
        String objectName = HoneyFileUtil.buildObjectNameByFileKey(fileVo.getFileName(), fileVo.getFileKey());
        honeyMiniO.upload(bucketName, objectName, fileVo.getHoneyStream().getInputStream(), contentType);
        return fileVo.getFileKey();
    }

    /**
     * 异步文件上传(不分片)
     *
     * @param fileVo      文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @param callbackUrl 回调url
     */
    @Override
    public void asyncUpload(FileVo fileVo, String bucketName, MediaType contentType, String callbackUrl) {
        // TODO 优化引入异步处理策略（1、http接口回调 2、MQ接受 3、短信等消息接受）
        // 参数校验
        ArgsCheckUtil.checkFile(fileVo, false);
        // 上传前检查一下服务里是否有其他用户已经上传过该文件，如果有，则实现秒传
        if (secondTransCheck(fileVo, bucketName)) {
            CallBack callBack = CallBackUtil.buildCallback(fileVo.getFileKey(), callbackUrl, 200, "success");
            CallbackFactory.getCallbackService(getByCode(callbackType)).callBack(callBack);
            return;
        }
        // 创建文件信息
        FilePo filePo = BeanConvertUtil.convert(fileVo, FilePo.class);
        filePo.setBucketName(bucketName);
        // 插入到数据库
        fileMapper.addFile(filePo);
        // 异步上传
        asyncTask.asyncUpload(fileVo, bucketName, contentType, callbackUrl);
    }

    /**
     * 分片上传
     *
     * @param fileVo      文件
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 是否成功
     * @deprecated since v1.0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadByShard(FileVo fileVo, String bucketName, MediaType contentType) {
        // 参数校验
        ArgsCheckUtil.checkFile(fileVo, true);
        // 是不是第一次分片上传
        boolean isExit = fileMapper.getFileByFileKeys(Collections.singletonList(fileVo.getFileKey())).size() > 0;
        if (!isExit) {
            // 创建文件信息
            FilePo filePo = BeanConvertUtil.convert(fileVo, FilePo.class);
            // 插入到数据库
            fileMapper.addFile(filePo);
        }
        FileShardVo fileShardVo = fileVo.getFileShardVos().get(0);
        // 上传至MiniO
        String objectName = HoneyFileUtil.buildObjectNameByFileKey(fileShardVo.getShardName(), fileShardVo.getFileKey());
        honeyMiniO.upload(bucketName, objectName, fileShardVo.getHoneyStream().getInputStream(), contentType);
        // 上传分片到数据库
        fileShardVo.setShardState(FileState.SUCCESS.getStateCode());
        fileShardService.addFileShard(fileShardVo);
        return fileVo.getFileKey();
    }


    /**
     * 异步分片上传
     *
     * @param fileVo      文件
     * @param bucketName  桶名
     * @param contentType contentType
     */
    @Override
    public void asyncUploadByShard(FileVo fileVo, String bucketName, MediaType contentType, String callbackUrl) {
        // TODO 引入异步处理策略（1、http接口回调 2、MQ接受 3、短信等消息接受）
        // 参数校验
        ArgsCheckUtil.checkFile(fileVo, true);
        // 是不是第一次分片上传
        boolean isExit = fileMapper.getFileByFileKeys(Collections.singletonList(fileVo.getFileKey())).size() > 0;
        if (!isExit) {
            // 创建文件信息
            FilePo filePo = BeanConvertUtil.convert(fileVo, FilePo.class);
            // 插入到数据库
            fileMapper.addFile(filePo);
        }
        // 上传分片
        FileShardVo fileShardVo = fileVo.getFileShardVos().get(0);
        fileShardService.addFileShard(fileShardVo);
        // 异步上传分片
        asyncTask.asyncUpload(fileShardVo, bucketName, contentType, callbackUrl);
    }

    /**
     * 异步上传文件（不分片）
     *
     * @param file        file
     * @param bucketName  桶名
     * @param contentType contentType
     * @param callbackUrl 回调url
     */
    @Override
    public void asyncUpload(File file, String bucketName, MediaType contentType, String callbackUrl) {
        // 转换成fileVo
        FileVo fileVo = BeanConvertUtil.convertFileVo(file);
        asyncUpload(fileVo, bucketName, contentType, callbackUrl);
    }

    /**
     * 文件上传（不分片）
     *
     * @param file        文件实体
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String upload(File file, String bucketName, MediaType contentType) {
        // 转换成fileVo
        FileVo fileVo = BeanConvertUtil.convertFileVo(file);
        fileVo.setBucketName(bucketName);
        return upload(fileVo, bucketName, contentType);
    }

    /**
     * 下载为url
     *
     * @param bucketName 桶名
     * @param fileKey    文件fileKey
     * @return string 文件的url
     */
    @Override
    public String downAsUrl(String bucketName, String fileKey) {
        String objectName = objectName(fileKey, bucketName);
        return honeyMiniO.downAsUrl(bucketName, objectName);
    }

    /**
     * 下载为url
     *
     * @param bucketName 桶名
     * @param fileKey    文件fileKey
     * @param expires    过期时间(秒)
     * @return string 文件的url
     */
    @Override
    public String downAsUrl(String bucketName, String fileKey, Integer expires) {
        String objectName = objectName(fileKey, bucketName);
        return honeyMiniO.downAsUrl(bucketName, objectName, expires);
    }

    /**
     * 下载为文件流
     *
     * @param bucketName 桶名
     * @param fileKey    文件fileKey
     * @return InputStream 文件流
     */
    @Override
    public InputStream downAsStream(String bucketName, String fileKey) {
        String objectName = objectName(fileKey, bucketName);
        return honeyMiniO.downAsStream(bucketName, objectName);
    }

    /**
     * 下载为文件流
     *
     * @param bucketName 桶名
     * @param fileKey    fileKey
     * @return HoneyStream 文件流
     */
    @Override
    public HoneyStream downAsHoneyStream(String bucketName, String fileKey) {
        InputStream inputStream = downAsStream(bucketName, fileKey);
        return new HoneyStream(inputStream);
    }

    /**
     * 下载至本地
     *
     * @param bucketName   桶名
     * @param fileKey      文件钥匙
     * @param fileDownPath 指定下载到本地的文件目录
     */
    @Override
    public void down2Local(String bucketName, String fileKey, String fileDownPath) {
        String objectName = objectName(fileKey, bucketName);
        honeyMiniO.down2Local(bucketName, objectName, fileDownPath);
    }

    @Override
    public List<FileVo> getFileByIds(List<String> ids) {
        List<FilePo> filePos = fileMapper.getFileByIds(ids);
        return mergeFileShad(filePos);
    }

    @Override
    public List<FileVo> getFileByFileKeys(List<String> fileKeys) {
        List<FilePo> filePos = fileMapper.getFileByFileKeys(fileKeys);
        return mergeFileShad(filePos);
    }

    @Override
    public boolean updateFileName(String fileId, String fileName) {
        return fileMapper.updateFileName(fileId, fileName);
    }

    @Override
    public boolean deleteFileByIds(List<String> ids) {
        return fileMapper.deleteFileByIds(ids);
    }

    @Override
    public boolean deletedFileByFileKeys(List<String> fileKeys) {
        return fileMapper.deletedFileByFileKeys(fileKeys);
    }

    /**
     * 更新文件状态
     *
     * @param fileKey   文件key
     * @param fileState 文件状态
     * @return 是否成功
     */
    @Override
    public boolean updateFileState(String fileKey, FileState fileState) {
        return fileMapper.updateFileState(fileKey, fileState.getStateCode());
    }

    /**
     * @param image         图片源
     * @param bucketName    桶名
     * @param contentType   contentType
     * @param needThumbnail 是否需要缩略图
     * @return 原图fileKey
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadImage(File image, String bucketName, MediaType contentType, boolean needThumbnail) {
        // 先上传原图
        String fileKey = upload(image, bucketName, contentType);
        if (needThumbnail) {
            Thumbnail defaultThumbnail = thumbnailService.defaultThumbnail(image);
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
    @Override
    public String uploadImage(File image, String bucketName, MediaType contentType, Thumbnail thumbnail) {
        // 先上传原图
        String fileKey = upload(image, bucketName, contentType);
        thumbnailService.buildThumbnail(thumbnail);
        thumbnailHandle(bucketName, contentType, fileKey, thumbnail);
        return fileKey;
    }

    /**
     * 将分片信息拼入文件并进行转化
     *
     * @param sources 文件源
     * @return 含有分片信息的文件
     */
    private List<FileVo> mergeFileShad(List<FilePo> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        }
        Map<String, List<FileShardVo>> rs = fileShardService.getFileShardInfoByFileKeys(
                sources.stream()
                        .map(FilePo::getFileKey)
                        .collect(Collectors.toList()));
        List<FileVo> fileVos = BeanConvertUtil.convertByList(sources, FileVo.class);
        fileVos.forEach(fileVo -> fileVo.setFileShardVos(rs.get(fileVo.getFileKey())));
        return fileVos;
    }


    /**
     * 是否秒传检查
     */
    private boolean secondTransCheck(FileVo fileVo, String bucketName) {
        return fileMapper.getFileByFileKeys(
                Collections.singletonList(fileVo.getFileKey()))
                .stream()
                .anyMatch(e -> e.getFileState() == FileState.SUCCESS.getStateCode() && bucketName.equals(e.getBucketName())
                );
    }

    /**
     * 通过fileKey构建objectName
     *
     * @param fileKey 文件fileKey
     * @return objectName
     */
    private String objectName(String fileKey, String bucketName) {
        String fileName = getFileByFileKeys(Collections.singletonList(fileKey))
                .stream()
                .filter(e -> e.getBucketName().equals(bucketName))
                .map(FileVo::getFileName)
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
        FileVo fileVo = BeanConvertUtil.convertFileVo(thumbnail);
        String thumbnailFileKey = HoneyFileUtil.getThumbnailFileKey(fileKey);
        fileVo.setFileKey(thumbnailFileKey);
        fileVo.setBucketName(bucketName);
        // 上传缩略图
        upload(fileVo, bucketName, contentType);
    }

}
