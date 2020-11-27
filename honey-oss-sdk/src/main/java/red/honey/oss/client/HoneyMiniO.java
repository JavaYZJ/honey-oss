package red.honey.oss.client;

import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import red.honey.oss.api.dto.FileShardDto;
import red.honey.oss.api.utils.HoneyFileUtil;
import red.honey.oss.utils.HoneyIOUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yangzhijie
 * @date 2020/11/4 10:52
 */
@Slf4j
public class HoneyMiniO {

    @Autowired
    MinioClient minioClient;

    /**
     * 上传(默认分段上传)
     *
     * @param bucketName  桶名
     * @param objectName  对象名
     * @param filePath    文件路径
     * @param contentType contentTyp
     */
    public void upload(String bucketName, String objectName, String filePath, MediaType contentType) {
        try {
            checkBucket(bucketName);
            minioClient.uploadObject(UploadObjectArgs.builder().bucket(bucketName).object(objectName).filename(filePath).contentType(contentType.toString()).build());
        } catch (Exception e) {
            log.warn("上传失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传（大于5M时会分段上传）
     *
     * @param bucketName  桶名
     * @param objectName  对象名
     * @param size        文件大小
     * @param inputStream 文件流
     * @param contentType contentType
     */
    public void upload(String bucketName, String objectName, long size, InputStream inputStream, MediaType contentType) {
        try {
            checkBucket(bucketName);
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(inputStream, size, -1).contentType(contentType.toString()).build());
        } catch (Exception e) {
            log.warn("上传失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            HoneyIOUtil.closeQuietly(inputStream);
        }
    }


    /**
     * 上传
     *
     * @param bucketName  桶名
     * @param objectName  对象名
     * @param inputStream 输入流
     * @param contentType contentType
     */
    public void upload(String bucketName, String objectName, InputStream inputStream, MediaType contentType) {
        try {
            checkBucket(bucketName);
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(inputStream, inputStream.available(), -1).contentType(contentType.toString()).build());
        } catch (Exception e) {
            log.warn("上传失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            HoneyIOUtil.closeQuietly(inputStream);
        }
    }


    /**
     * 上传
     *
     * @param bucketName  桶名
     * @param objectName  对象名
     * @param inputStream 输入流
     * @param contentType contentType
     * @return string url 直接点击该url即可在浏览器中下载文件
     */
    public String uploadAndGetUrl(String bucketName, String objectName, InputStream inputStream, MediaType contentType) {
        try {
            checkBucket(bucketName);
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(inputStream, inputStream.available(), -1).contentType(contentType.toString()).build());
            return downAsUrl(bucketName, objectName);
        } catch (Exception e) {
            log.warn("上传失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            HoneyIOUtil.closeQuietly(inputStream);
        }
    }

    /**
     * 下载文件至指定的本地文件路径
     *
     * @param bucketName   桶名
     * @param objectName   对象名
     * @param fileDownPath 文件存储路径
     */
    public void down2Local(String bucketName, String objectName, String fileDownPath) {
        try {
            minioClient.downloadObject(DownloadObjectArgs.builder().bucket(bucketName).object(objectName).filename(fileDownPath).build());
        } catch (Exception e) {
            log.warn("下载失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载为文件流
     *
     * @param bucketName 桶名
     * @param objectName 对象名
     * @return InputStream 文件流
     */
    public InputStream downAsStream(String bucketName, String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            log.warn("下载失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * 下载为url（默认过期时间7天）
     *
     * @param bucketName 桶名
     * @param objectName 对象名
     * @return string 文件的url
     */
    public String downAsUrl(String bucketName, String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            log.warn("下载失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载为url
     *
     * @param bucketName 桶名
     * @param objectName 对象名
     * @param duration   过期时间
     * @param unit       单位
     * @return string 文件的url
     */
    public String downAsUrl(String bucketName, String objectName, int duration, TimeUnit unit) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(duration, unit)
                            .build());
        } catch (Exception e) {
            log.warn("下载失败：原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 分片合并
     *
     * @param bucketName    桶名
     * @param fileShardDtos 分片数据
     * @param objectName    合并后的文件的objectName
     * @return ObjectWriteResponse
     */
    public ObjectWriteResponse compose(String bucketName, List<FileShardDto> fileShardDtos, String objectName) {
        List<ComposeSource> sourceObjectList = new ArrayList<>();
        fileShardDtos.forEach(fileShardDto -> {
            String shardObjectName = HoneyFileUtil.buildObjectNameByFileKey(fileShardDto.getShardName(), fileShardDto.getFileKey());
            sourceObjectList.add(
                    ComposeSource.builder().bucket(bucketName)
                            .object(shardObjectName)
                            .build()
            );
        });
        try {
            return minioClient.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .sources(sourceObjectList)
                            .build());
        } catch (Exception e) {
            // todo 记录起来，后续做补偿机制
            throw new RuntimeException(e);
        }
    }

    private void checkBucket(String bucketName) throws Exception {
        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isExist) {
            // 创建一个名为bucketName的存储桶，用于存储文件。
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }
}
