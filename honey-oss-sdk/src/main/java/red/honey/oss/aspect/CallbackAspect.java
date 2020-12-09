package red.honey.oss.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import red.honey.oss.annotation.Callback;
import red.honey.oss.api.dto.FileDto;
import red.honey.oss.api.entiy.CallBack;
import red.honey.oss.api.service.dubbo.PureFileRpcService;
import red.honey.oss.api.utils.HoneyFileUtil;
import red.honey.oss.strategy.CallbackStrategy;
import red.honey.oss.task.AsyncTask;
import red.honey.oss.utils.AnnotationUtils;

import java.io.File;

import static red.honey.oss.utils.AnnotationUtils.getAnnotation;
import static red.honey.oss.utils.AnnotationUtils.hasAnnotation;

/**
 * @author yangzhijie
 * @date 2020/11/4 16:41
 */
@Aspect
@Order(1)
@Slf4j
public class CallbackAspect implements ApplicationContextAware {


    @Reference(version = "1.0")
    private PureFileRpcService postFileRpcService;

    private ApplicationContext applicationContext;

    @Autowired
    private Environment environment;

    @Autowired
    private AsyncTask asyncTask;

    @Pointcut("@annotation(red.honey.oss.annotation.Callback)")
    public void callbackPointcut() {
    }

    @Around("callbackPointcut()")
    public void callbackPointcutProcess(ProceedingJoinPoint jointPoint) {
        boolean hasAnnotation = hasAnnotation(jointPoint, Callback.class);
        if (hasAnnotation) {
            Callback annotation = (Callback) getAnnotation(jointPoint, Callback.class);
            Class<? extends CallbackStrategy> type = annotation.type();
            CallbackStrategy bean;
            try {
                bean = applicationContext.getBean(type);
            } catch (NoUniqueBeanDefinitionException nue) {
                throw nue;
            } catch (NoSuchBeanDefinitionException nbe) {
                bean = applicationContext.getBean(CallbackStrategy.class);
            }
            Object[] args = jointPoint.getArgs();
            File file = null;
            String bucketName = null;
            MediaType contentType = null;
            CallBack callBack = new CallBack();
            for (Object arg : args) {
                if (arg instanceof File) {
                    file = (File) arg;
                } else if (arg instanceof String) {
                    bucketName = (String) arg;
                } else if (arg instanceof MediaType) {
                    contentType = (MediaType) arg;
                } else if (arg instanceof CallBack) {
                    callBack = (CallBack) arg;
                }
            }
            Assert.state(!(file == null && StringUtils.isEmpty(bucketName) && contentType == null), "file and bucketName or contentType all must not be null or empty");
            FileDto fileDto = HoneyFileUtil.convertFileDto(file);
            fileDto.setBucketName(bucketName);
            if (AnnotationUtils.conditionOnSecondTrans(jointPoint, applicationContext, environment)) {
                callbackOnSecondTrans(callBack, fileDto.getFileKey(), bean);
            } else {
                // Persistence to db
                postFileRpcService.postFileInfo(fileDto);
                try {
                    jointPoint.proceed();
                } catch (Throwable throwable) {
                    log.error("aop happen error {}", throwable.getMessage());
                }
                asyncTask.asyncUpload(fileDto, bucketName, contentType, callBack, bean);
            }
        }
    }

    private void callbackOnSecondTrans(CallBack callBack, String data, CallbackStrategy bean) {
        callBack.setMsg("秒传success");
        callBack.setCode(200);
        callBack.setData(data);
        bean.callbackProcess(callBack);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
