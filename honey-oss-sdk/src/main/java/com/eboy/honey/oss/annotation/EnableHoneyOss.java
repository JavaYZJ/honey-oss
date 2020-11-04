package com.eboy.honey.oss.annotation;

import com.eboy.honey.oss.config.HoneyOssAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author yangzhijie
 * @date 2020/11/4 10:37
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(HoneyOssAutoConfiguration.class)
public @interface EnableHoneyOss {
}
