package red.honey.oss.job.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yangzhijie
 * @date 2020/9/30 11:56
 */
@Data
@Component
@ConfigurationProperties(prefix = "honey.oss")
public class HoneyOssProperties {

    private long appId;

    private String appKey;

    private String pornUrl;

    private String terrorismUrl;

    private String evilaudioUrl;
}
