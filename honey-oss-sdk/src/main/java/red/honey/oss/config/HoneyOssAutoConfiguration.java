package red.honey.oss.config;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import red.honey.oss.api.HoneyOss;
import red.honey.oss.aspect.CallbackAspect;
import red.honey.oss.aspect.SecondTransAspect;
import red.honey.oss.client.HoneyMiniO;
import red.honey.oss.properties.MinioProperties;
import red.honey.oss.strategy.CallbackStrategy;
import red.honey.oss.strategy.SecondTransStrategy;
import red.honey.oss.strategy.impl.Md5DigestAsHex;
import red.honey.oss.strategy.impl.RestCallback;
import red.honey.oss.task.AsyncTask;

import static io.minio.MinioClient.builder;

/**
 * @author yangzhijie
 * @date 2020/11/4 10:38
 */
@Slf4j
@Lazy
@Configuration
@ConditionalOnClass(HoneyOss.class)
@Import({RestTemplateConfig.class, AsyncConfig.class})
public class HoneyOssAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public HoneyOss honeyOss() {
        return new HoneyOss();
    }


    @Bean
    @ConditionalOnMissingBean
    public HoneyMiniO honeyMiniO() {
        return new HoneyMiniO();
    }

    @Bean
    public MinioProperties minioProperties() {
        return new MinioProperties();
    }

    @Bean
    @ConditionalOnMissingBean(MinioClient.class)
    public MinioClient minioClient(@Qualifier("minioProperties") MinioProperties minioProperties) {
        return builder().endpoint(minioProperties.getUrl()).credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey()).build();
    }

    @Bean
    public SecondTransAspect secondTransAspect() {
        return new SecondTransAspect();
    }

    @Bean
    public CallbackAspect callbackAspect() {
        return new CallbackAspect();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(Md5DigestAsHex.class)
    public SecondTransStrategy secondTransStrategy() {
        return new Md5DigestAsHex();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(RestCallback.class)
    public CallbackStrategy callbackStrategy() {
        return new RestCallback();
    }


    @Bean
    public AsyncTask asyncTask() {
        return new AsyncTask();
    }
}
