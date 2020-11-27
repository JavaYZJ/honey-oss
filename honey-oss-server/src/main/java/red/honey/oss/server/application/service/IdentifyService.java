package red.honey.oss.server.application.service;

/**
 * @author yangzhijie
 * @date 2020/10/12 17:34
 */
public interface IdentifyService {


    /**
     * 检测
     *
     * @param bucketName 桶名
     * @param fileKey    fileKey
     * @return 检测结果
     */
    String identify(String bucketName, String fileKey);
}
