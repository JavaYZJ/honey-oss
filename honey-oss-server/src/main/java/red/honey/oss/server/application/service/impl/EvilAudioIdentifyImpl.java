package red.honey.oss.server.application.service.impl;

import cn.xsshome.taip.vision.TAipVision;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import red.honey.oss.server.application.service.FileService;
import red.honey.oss.server.application.service.IdentifyService;

/**
 * @author yangzhijie
 * @date 2020/10/12 17:45
 */
@Service
@Slf4j
public class EvilAudioIdentifyImpl implements IdentifyService {

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
        String url = fileService.downAsUrl(bucketName, fileKey);
        TAipVision aipVision = new TAipVision(appId, appKey);
        try {

            return aipVision.aaiEvilAudio(fileKey, url);
        } catch (Exception e) {
            log.warn("bucketName:{},fileKey:{} 执行音频检测失败，原因：{}", bucketName, fileKey, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
