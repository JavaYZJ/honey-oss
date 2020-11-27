package red.honey.oss.annotation;

import red.honey.oss.strategy.SecondTransStrategy;
import red.honey.oss.strategy.impl.Md5DigestAsHex;

import java.lang.annotation.*;

/**
 * @author yangzhijie
 * @date 2020/11/4 15:37
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecondTrans {

    /**
     * 秒传策略实现类
     * 默认文件MD5
     */
    Class<? extends SecondTransStrategy> value() default Md5DigestAsHex.class;

    String bucketName();


}
