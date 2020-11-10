package com.eboy.honey.oss.aspect;

import com.eboy.honey.oss.annotation.SecondTrans;
import com.eboy.honey.oss.api.dto.FileDto;
import com.eboy.honey.oss.utils.AnnotationUtils;
import com.eboy.honey.oss.utils.BeanConverter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.io.File;

import static com.eboy.honey.oss.utils.AnnotationUtils.getFile;
import static com.eboy.honey.oss.utils.AnnotationUtils.hasAnnotation;

/**
 * @author yangzhijie
 * @date 2020/11/4 16:41
 */
@Aspect
@Order(2)
@Slf4j
public class SecondTransAspect implements ApplicationContextAware {

    @Autowired
    private Environment environment;
    private ApplicationContext applicationContext;

    @Pointcut("@annotation(com.eboy.honey.oss.annotation.SecondTrans) && !@annotation(com.eboy.honey.oss.annotation.Callback)")
    public void secondTransPointcut() {
    }

    @Around("secondTransPointcut()")
    public Object secondTransProcess(ProceedingJoinPoint jointPoint) {
        boolean hasAnnotation = hasAnnotation(jointPoint, SecondTrans.class);
        if (hasAnnotation) {
            boolean conditionOnSecondTrans = AnnotationUtils.conditionOnSecondTrans(jointPoint, applicationContext, environment);
            try {
                if (!conditionOnSecondTrans) {
                    return jointPoint.proceed();
                } else {
                    File file = getFile(jointPoint);
                    FileDto fileDto = BeanConverter.convert2FileDto(file);
                    return fileDto.getFileKey();
                }
            } catch (Throwable throwable) {
                log.error("aop happen error {}", throwable.getMessage());
            }
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
