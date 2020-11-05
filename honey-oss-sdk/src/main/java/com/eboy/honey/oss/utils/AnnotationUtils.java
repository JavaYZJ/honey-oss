package com.eboy.honey.oss.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.eboy.honey.oss.utils.ParserUtils.isSpelExpression;

/**
 * @author yangzhijie
 * @date 2020/11/5 14:09
 */
public class AnnotationUtils {


    public static <A extends Annotation> boolean isPresent(Method method, Class<A> annotationClass) {
        return org.apache.dubbo.config.spring.util.AnnotationUtils.isPresent(method, annotationClass);
    }

    public static <A extends Annotation> Annotation getAnnotation(Method method, Class<A> annotationClass) {
        return org.springframework.core.annotation.AnnotationUtils.getAnnotation(method, annotationClass);
    }

    public static <A extends Annotation> Annotation getAnnotation(Annotation annotation, Class<A> annotationClass) {
        return org.springframework.core.annotation.AnnotationUtils.getAnnotation(annotation, annotationClass);
    }

    public static String resolvePlaceholders(String attributeValue, PropertyResolver propertyResolver) {
        String resolvedValue = attributeValue;
        if (propertyResolver != null) {
            resolvedValue = propertyResolver.resolvePlaceholders(attributeValue);
            resolvedValue = StringUtils.trimWhitespace(resolvedValue);
        }

        return resolvedValue;
    }

    public static Map<String, Object> resolvePlaceholders(Map<String, Object> sourceAnnotationAttributes, PropertyResolver propertyResolver, String... ignoreAttributeNames) {
        return org.apache.dubbo.config.spring.util.AnnotationUtils.resolvePlaceholders(sourceAnnotationAttributes, propertyResolver, ignoreAttributeNames);
    }

    public static Map<String, Object> resolveSpel(Map<String, Object> sourceAnnotationAttributes, ProceedingJoinPoint joinPoint) {
        Map<String, Object> temp = new HashMap<>(sourceAnnotationAttributes.size());
        sourceAnnotationAttributes.forEach((key, value) -> {
            if (value instanceof String) {
                if (isSpelExpression((String) value)) {
                    String spel = ParserUtils.parseSpel((String) value, joinPoint);
                    temp.put(key, spel);
                } else {
                    temp.put(key, value);
                }
            }
        });
        return temp;
    }


}
