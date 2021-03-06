package red.honey.oss.server.application.service.impl;

import cn.xsshome.taip.vision.TAipVision;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import red.honey.oss.server.application.service.FileService;
import red.honey.oss.server.application.service.IdentifyService;

import java.io.InputStream;

/**
 * @author yangzhijie
 * @date 2020/10/12 17:36
 */
@Service
@Slf4j
public class PornIdentifyImpl implements IdentifyService {

    @Value("${honey.oss.appId}")
    private String appId;
    @Value("${honey.oss.appKey}")
    private String appKey;

    @Autowired
    private FileService fileService;

    /**
     * 检测
     *
     * @param bucketName 桶名
     * @param fileKey    fileKey
     * @return 检测结果
     */
    @Override
    public String identify(String bucketName, String fileKey) {
        TAipVision aipVision = new TAipVision(appId, appKey);
        try (InputStream inputStream = fileService.downAsStream(bucketName, fileKey)) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return aipVision.visionPorn(bytes);
        } catch (Exception e) {
            log.warn("bucketName:{},fileKey:{} 执行鉴黄失败，原因：{}", bucketName, fileKey, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
