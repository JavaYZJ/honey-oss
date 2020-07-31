package com.eboy.honey.oss.server.autoconfig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangzhijie
 * @date 2020/7/28 15:48
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "honey.oss.minio")
public class MinioProperties {

    /**
     * minio 服务地址
     */
    private String url;
    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;
    /**
     * 桶名称
     */
    private String bucketName;

}
