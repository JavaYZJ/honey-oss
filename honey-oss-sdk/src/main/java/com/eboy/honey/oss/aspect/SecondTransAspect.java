package com.eboy.honey.oss.aspect;

import com.eboy.honey.oss.annotation.SecondTrans;
import com.eboy.honey.oss.api.dto.FileDto;
import com.eboy.honey.oss.strategy.SecondTransStrategy;
import com.eboy.honey.oss.utils.AnnotationUtils;
import com.eboy.honey.oss.utils.BeanConverter;
import com.eboy.honey.oss.utils.ParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author yangzhijie
 * @date 2020/11/4 16:41
 */
@Aspect
@Slf4j
public class SecondTransAspect implements ApplicationContextAware {

    @Autowired
    private Environment environment;
    private ApplicationContext applicationContext;

    @Pointcut("@annotation(com.eboy.honey.oss.annotation.SecondTrans)")
    public void secondTransPointcut() {
    }

    @Around("secondTransPointcut()")
    public Object secondTransProcess(ProceedingJoinPoint jointPoint) {
        boolean hasAnnotation = hasAnnotation(jointPoint, SecondTrans.class);
        if (hasAnnotation) {
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
            } catch (NoSuchBeanDefinitionException e) {
                // specially SecondTransStrategy bean
                strategyBean = applicationContext.getBean(SecondTransStrategy.class);
            }
            File file = getFile(jointPoint);
            FileDto fileDto = BeanConverter.convert2FileDto(file);
            boolean conditionOnSecondTrans = strategyBean.conditionOnSecondTrans(bucketNameValue, file);
            try {
                if (!conditionOnSecondTrans) {
                    return jointPoint.proceed();
                } else {
                    log.debug("秒传成功,使用的秒传策略：{}", clazz);
                    return fileDto.getFileKey();
                }
            } catch (Throwable throwable) {
                log.error("aop happen error {}", throwable.getMessage());
            }
        }
        return null;
    }

    private File getFile(ProceedingJoinPoint jointPoint) {
        Object[] arguments = jointPoint.getArgs();
        return (File) Arrays.stream(arguments).filter(e -> e instanceof File)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("the aop method not found File"));
    }

    private Method getMethod(ProceedingJoinPoint jointPoint) {
        return ((MethodSignature) jointPoint.getSignature()).getMethod();
    }

    private boolean hasAnnotation(ProceedingJoinPoint jointPoint, Class<? extends SecondTrans> clazz) {
        Annotation annotation = getAnnotation(jointPoint, clazz);
        return annotation != null;
    }

    public Annotation getAnnotation(ProceedingJoinPoint jointPoint, Class<? extends SecondTrans> clazz) {
        Method method = ((MethodSignature) jointPoint.getSignature()).getMethod();
        return method.getAnnotation(clazz);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
