package red.honey.oss.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import red.honey.oss.api.constant.CallbackConstant;
import red.honey.oss.api.constant.FileState;
import red.honey.oss.api.dto.FileDto;
import red.honey.oss.api.dto.FileShardDto;
import red.honey.oss.api.entiy.CallBack;
import red.honey.oss.api.service.dubbo.FileRpcService;
import red.honey.oss.api.utils.CallBackUtil;
import red.honey.oss.api.utils.HoneyFileUtil;
import red.honey.oss.client.HoneyMiniO;
import red.honey.oss.strategy.CallbackStrategy;

import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/8/12 16:42
 */
@Component
@Slf4j
public class AsyncTask {

    @Reference(version = "1.0")
    private FileRpcService fileService;
    @Autowired
    private HoneyMiniO honeyMiniO;


    /**
     * 异步上传
     */
    @Async("asyncExecutor")
    public void asyncUpload(FileDto file, String bucketName, MediaType contentType,
                            CallBack callBack, CallbackStrategy callbackStrategy) {
        String msg = CallbackConstant.CALL_BACK_SUCCESS_MSG;
        String data = file.getFileKey();
        int code = CallbackConstant.CALL_BACK_SUCCESS_CODE;
        try {
            // 上传至Minio
            String objectName = HoneyFileUtil.buildObjectNameByFileKey(file.getFileName(), file.getFileKey());
            honeyMiniO.upload(bucketName, objectName, file.getHoneyStream().getInputStream(), contentType);
            // 上传成功后，修改该文件状态为 成功
            fileService.updateFileState(file.getFileKey(), FileState.SUCCESS);
        } catch (Exception e) {
            code = CallbackConstant.CALL_BACK_FAIL_CODE;
            msg = e.getMessage();
            data = null;
        } finally {
            // 回调
            callBack = CallBackUtil.buildCallback(data, callBack.getCallBackHttpUrl(), code, msg);
            callbackStrategy.callbackProcess(callBack);
        }
    }

    @Async("asyncExecutor")
    public void composeShard(String bucketName, List<FileShardDto> fileShardDtos, String objectName) {
        honeyMiniO.compose(bucketName, fileShardDtos, objectName);
    }

}
