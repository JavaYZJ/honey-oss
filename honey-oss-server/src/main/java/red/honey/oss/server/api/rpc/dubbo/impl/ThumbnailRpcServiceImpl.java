package red.honey.oss.server.api.rpc.dubbo.impl;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import red.honey.oss.api.entiy.Thumbnail;
import red.honey.oss.api.service.dubbo.ThumbnailRpcService;
import red.honey.oss.server.application.service.ThumbnailService;

/**
 * @author yangzhijie
 * @date 2020/11/11 10:50
 */
@Service(version = "1.0")
public class ThumbnailRpcServiceImpl implements ThumbnailRpcService {

    @Autowired
    private ThumbnailService thumbnailService;

    /**
     * 构建缩略图
     *
     * @param thumbnail 构建规则
     */
    @Override
    public Thumbnail buildThumbnail(Thumbnail thumbnail) {
        thumbnailService.buildThumbnail(thumbnail);
        return thumbnail;
    }

    /**
     * 根据原图获取缩略图链接
     *
     * @param bucketName 桶名
     * @param fileKey    原图fileKey
     * @return 缩略图链接
     */
    @Override
    public String getUrlByOriginalPicture(String bucketName, String fileKey) {
        return thumbnailService.getUrlByOriginalPicture(bucketName, fileKey);
    }

    /**
     * 根据原图获取缩略图链接
     *
     * @param bucketName 桶名
     * @param fileKey    原图fileKey
     * @param expires    过期时间（秒）
     * @return 缩略图链接
     */
    @Override
    public String getUrlByOriginalPicture(String bucketName, String fileKey, Integer expires) {
        return thumbnailService.getUrlByOriginalPicture(bucketName, fileKey, expires);
    }
}
