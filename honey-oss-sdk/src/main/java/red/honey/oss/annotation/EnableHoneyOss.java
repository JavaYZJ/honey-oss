package red.honey.oss.annotation;

import org.springframework.context.annotation.Import;
import red.honey.oss.config.HoneyOssAutoConfiguration;

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
