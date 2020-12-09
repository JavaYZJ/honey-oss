package red.honey.oss.server.autoconfig.config;


import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import red.honey.oss.server.autoconfig.properties.MinioProperties;
import red.honey.oss.server.client.HoneyMiniO;

/**
 * @author yangzhijie
 * @date 2020/7/28 15:45
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({MinioProperties.class})
public class HoneyOssAutoConfiguration {
    @Autowired
    private MinioProperties minioProperties;

    @Bean
    @ConditionalOnMissingBean(MinioClient.class)
    public MinioClient minioClient() {
        try {
            return new MinioClient(
                    minioProperties.getUrl(),
                    minioProperties.getAccessKey(),
                    minioProperties.getSecretKey()
            );
        } catch (InvalidEndpointException | InvalidPortException e) {
            log.warn("实例化MinioClient失败，原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Bean
    public HoneyMiniO honeyMiniO() {
        return new HoneyMiniO();
    }
}
