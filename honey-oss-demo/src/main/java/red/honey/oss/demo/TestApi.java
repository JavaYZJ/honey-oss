package red.honey.oss.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import red.honey.oss.annotation.Callback;
import red.honey.oss.annotation.SecondTrans;
import red.honey.oss.api.HoneyOss;
import red.honey.oss.api.entiy.CallBack;

import java.io.File;

/**
 * @author yangzhijie
 * @date 2020/11/6 9:45
 */
@Component
public class TestApi {

    @Autowired
    HoneyOss honeyOss;

    @SecondTrans(value = SecondTrans2Impl.class, bucketName = "${honey.oss.bucketName}")
    public String upload(File file, String bucketName, MediaType contentType) {
        return honeyOss.upload(file, bucketName, contentType).getFileKey();
    }

    @Callback
    @SecondTrans(value = SecondTrans2Impl.class, bucketName = "${honey.oss.bucketName}")
    public void asyncUpload(File file, String bucketName, MediaType contentType, CallBack callBack) {
        honeyOss.asyncUpload(file, bucketName, contentType, callBack);
    }
}
