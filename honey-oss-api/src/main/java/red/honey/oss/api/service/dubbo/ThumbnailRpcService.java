package red.honey.oss.api.service.dubbo;

import red.honey.oss.api.constant.ImageType;
import red.honey.oss.api.entiy.OutputMode;
import red.honey.oss.api.entiy.Thumbnail;
import red.honey.oss.api.utils.FilePathUtil;

import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * @author yangzhijie
 * @date 2020/11/11 10:48
 */
public interface ThumbnailRpcService {

    /**
     * 构建缩略图
     *
     * @param thumbnail 构建规则
     * @return Thumbnail
     */
    Thumbnail buildThumbnail(@NotNull Thumbnail thumbnail);

    /**
     * 根据原图获取缩略图链接
     *
     * @param fileKey    原图fileKey
     * @param bucketName 桶名
     * @return 缩略图链接
     */
    String getUrlByOriginalPicture(String bucketName, String fileKey);

    /**
     * 根据原图获取缩略图链接
     *
     * @param fileKey    原图fileKey
     * @param bucketName 桶名
     * @param expires    过期时间（秒）
     * @return 缩略图链接
     */
    String getUrlByOriginalPicture(String bucketName, String fileKey, Integer expires);

    /**
     * 构建默认缩略图规则
     *
     * @param image 原图
     * @return 默认缩略图规则
     */
    default Thumbnail defaultThumbnail(File image) {
        String thumbnailPath = FilePathUtil.defaultThumbnailPath() + ImageType.JPEG;
        Thumbnail thumbnail = new Thumbnail();
        OutputMode outputMode = new OutputMode();
        outputMode.setFilePath(thumbnailPath);
        thumbnail.setOutputMode(outputMode);
        thumbnail.setInputSource(image);
        buildThumbnail(thumbnail);
        return thumbnail;
    }
}
