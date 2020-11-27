package red.honey.oss.job.application.config;

import cn.xsshome.taip.vision.TAipVision;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangzhijie
 * @date 2020/10/9 10:53
 */
@Configuration
public class TencentAIConfig {

    @Value("${honey.oss.app_id}")
    private String appId;
    @Value("${honey.oss.app_key}")
    private String appKey;

    @Bean
    public TAipVision tAipVision() {
        return new TAipVision(appId, appKey);
    }
}
