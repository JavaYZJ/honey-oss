package com.eboy.honey.oss.annotation;


import com.eboy.honey.oss.strategy.CallbackStrategy;
import com.eboy.honey.oss.strategy.impl.RestCallback;

import java.lang.annotation.*;

/**
 * @author yangzhijie
 * @date 2020/11/9 14:10
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Callback {

    /**
     * 回调类型
     */
    Class<? extends CallbackStrategy> type() default RestCallback.class;


}
