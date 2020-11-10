package com.eboy.honey.oss.utils;

import com.eboy.honey.oss.annotation.SecondTrans;
import com.eboy.honey.oss.strategy.SecondTransStrategy;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.StringUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.eboy.honey.oss.utils.ParserUtils.isSpelExpression;

/**
 * @author yangzhijie
 * @date 2020/11/5 14:09
 */
@Slf4j
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

    public static Method getMethod(ProceedingJoinPoint jointPoint) {
        return ((MethodSignature) jointPoint.getSignature()).getMethod();
    }

    public static boolean hasAnnotation(ProceedingJoinPoint jointPoint, Class<? extends Annotation> clazz) {
        Annotation annotation = getAnnotation(jointPoint, clazz);
        return annotation != null;
    }

    public static Annotation getAnnotation(ProceedingJoinPoint jointPoint, Class<? extends Annotation> clazz) {
        Method method = ((MethodSignature) jointPoint.getSignature()).getMethod();
        return method.getAnnotation(clazz);
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

    public static File getFile(ProceedingJoinPoint jointPoint) {
        Object[] arguments = jointPoint.getArgs();
        return (File) Arrays.stream(arguments).filter(e -> e instanceof File)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("the aop method not found File"));
    }

    public static boolean conditionOnSecondTrans(ProceedingJoinPoint jointPoint, ApplicationContext applicationContext, Environment environment) {
        boolean secondTrans = hasAnnotation(jointPoint, SecondTrans.class);
        if (secondTrans) {
            SecondTrans annotation = (SecondTrans) getAnnotation(jointPoint, SecondTrans.class);
            Class<? extends SecondTransStrategy> clazz = annotation.value();
            String bucketName = annotation.bucketName();
            String bucketNameValue = ParserUtils.parseSpel(bucketName, jointPoint);
            if (bucketNameValue.equals(bucketName)) {
                bucketNameValue = AnnotationUtils.resolvePlaceholders(bucketName, environment);
            }
            SecondTransStrategy strategyBean;
            try {
                // default SecondTransStrategy bean
                strategyBean = applicationContext.getBean(clazz);
            } catch (NoUniqueBeanDefinitionException nue) {
                throw nue;
            } catch (NoSuchBeanDefinitionException e) {
                // specially SecondTransStrategy bean
                strategyBean = applicationContext.getBean(SecondTransStrategy.class);
            }
            File file = getFile(jointPoint);
            boolean condition = strategyBean.conditionOnSecondTrans(bucketNameValue, file);
            if (condition) {
                log.debug("秒传成功,使用的秒传策略：{}", clazz);
                return true;
            }
        }
        return false;
    }
}
