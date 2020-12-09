package red.honey.oss.server.api.web.controller;

import com.eboy.honey.response.commmon.HoneyResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import red.honey.oss.server.application.service.IdentifyService;

import javax.annotation.Resource;

/**
 * @author yangzhijie
 * @date 2020/10/16 10:56
 */
@RestController
@RequestMapping("/v1/identify")
public class IdentifyController {

    @Resource(name = "pornIdentifyImpl")
    IdentifyService pornIdentify;
    @Resource(name = "terrorismIdentifyImpl")
    IdentifyService terrorismIdentify;
    @Resource(name = "evilAudioIdentifyImpl")
    IdentifyService evilAudioIdentify;


    /**
     * 图片鉴黄
     *
     * @param bucketName 桶名
     * @param fileKey    图片key
     */
    @GetMapping("/pron/{bucketName}/{fileKey}")
    public HoneyResponse<String> pornIdentify(@PathVariable String bucketName, @PathVariable String fileKey) {
        return HoneyResponse.success(pornIdentify.identify(bucketName, fileKey));
    }

    /**
     * 图片鉴暴
     *
     * @param bucketName 桶名
     * @param fileKey    图片key
     */
    @GetMapping("/terrorism/{bucketName}/{fileKey}")
    public HoneyResponse<String> terrorismIdentify(@PathVariable String bucketName, @PathVariable String fileKey) {
        return HoneyResponse.success(terrorismIdentify.identify(bucketName, fileKey));
    }

    /**
     * 音频检测（敏感词等）
     *
     * @param bucketName 桶名
     * @param fileKey    图片key
     */
    @GetMapping("/evilAudio/{bucketName}/{fileKey}")
    public HoneyResponse<String> evilAudioIdentify(@PathVariable String bucketName, @PathVariable String fileKey) {
        return HoneyResponse.success(evilAudioIdentify.identify(bucketName, fileKey));
    }
}
