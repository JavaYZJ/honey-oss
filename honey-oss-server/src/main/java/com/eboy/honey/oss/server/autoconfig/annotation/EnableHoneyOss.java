package com.eboy.honey.oss.server.autoconfig.annotation;

import com.eboy.honey.oss.server.autoconfig.config.HoneyOssAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author yangzhijie
 * @date 2020/7/28 15:44
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(HoneyOssAutoConfiguration.class)
public @interface EnableHoneyOss {
}
