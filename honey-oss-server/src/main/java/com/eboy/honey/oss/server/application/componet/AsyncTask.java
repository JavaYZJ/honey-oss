package com.eboy.honey.oss.server.application.componet;

import com.eboy.honey.oss.api.constant.CallbackConstant;
import com.eboy.honey.oss.api.constant.CallbackEnum;
import com.eboy.honey.oss.api.constant.FileState;
import com.eboy.honey.oss.api.entiy.CallBack;
import com.eboy.honey.oss.api.utils.CallBackUtil;
import com.eboy.honey.oss.api.utils.HoneyFileUtil;
import com.eboy.honey.oss.server.application.factory.CallbackFactory;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.service.FileShardService;
import com.eboy.honey.oss.server.application.vo.FileShardVo;
import com.eboy.honey.oss.server.application.vo.FileVo;
import com.eboy.honey.oss.server.client.HoneyMiniO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/8/12 16:42
 */
@Component
@Slf4j
public class AsyncTask {

    @Autowired
    FileService fileService;
    @Autowired
    private HoneyMiniO honeyMiniO;
    @Autowired
    private FileShardService fileShardService;
    @Value("${honey.oss.callback-type}")
    private int callbackType;

    /**
     * 分片异步上传
     */
    @Async("asyncExecutor")
    public void asyncUpload(FileShardVo fileShardVo, String bucketName, MediaType contentType, String callbackUrl) {
        String msg = CallbackConstant.CALL_BACK_SUCCESS_MSG;
        String data = fileShardVo.getFileKey();
        int code = CallbackConstant.CALL_BACK_SUCCESS_CODE;
        try {
            // 上传至Minio
            String objectName = HoneyFileUtil.buildObjectNameByFileKey(fileShardVo.getShardName(), fileShardVo.getFileKey());
            honeyMiniO.upload(bucketName, objectName, fileShardVo.getHoneyStream().getInputStream(), contentType);
            // 上传成功后，修改该分片状态为 成功
            fileShardService.updateFileShardState(fileShardVo.getFileKey(), FileState.SUCCESS);
        } catch (Exception e) {
            code = CallbackConstant.CALL_BACK_FAIL_CODE;
            msg = e.getMessage();
            data = null;
        } finally {
            // 回调
            CallBack callBack = CallBackUtil.buildCallback(data, callbackUrl, code, msg);
            CallbackFactory.getCallbackService(CallbackEnum.getByCode(callbackType)).callBack(callBack);
        }
    }

    /**
     * 异步上传
     */
    @Async("asyncExecutor")
    public void asyncUpload(FileVo fileVo, String bucketName, MediaType contentType, String callbackUrl) {
        String msg = CallbackConstant.CALL_BACK_SUCCESS_MSG;
        String data = fileVo.getFileKey();
        int code = CallbackConstant.CALL_BACK_SUCCESS_CODE;
        try {
            // 上传至Minio
            String objectName = HoneyFileUtil.buildObjectNameByFileKey(fileVo.getFileName(), fileVo.getFileKey());
            honeyMiniO.upload(bucketName, objectName, fileVo.getHoneyStream().getInputStream(), contentType);
            // 上传成功后，修改该文件状态为 成功
            fileService.updateFileState(fileVo.getFileKey(), FileState.SUCCESS);
        } catch (Exception e) {
            code = CallbackConstant.CALL_BACK_FAIL_CODE;
            msg = e.getMessage();
            data = null;
        } finally {
            // 回调
            CallBack callBack = CallBackUtil.buildCallback(data, callbackUrl, code, msg);
            CallbackFactory.getCallbackService(CallbackEnum.getByCode(callbackType)).callBack(callBack);
        }
    }

    /**
     * 异步检查是不是最后一块并修改文件的状态为成功
     *
     * @deprecated
     */
    @Async("asyncExecutor")
    public void asyncCheckAndMerge(FileShardVo fileShardVo) {
        List<FileVo> fileInfos = fileService.getFileByFileKeys(Collections.singletonList(fileShardVo.getFileKey()));
        if (!CollectionUtils.isEmpty(fileInfos)) {
            FileVo fileVo = fileInfos.get(0);
            // 文件总分片数
            int shardSize = fileVo.getShardTotal();
            // 该文件的分片已经处于成功的数量
            long count = fileVo.getFileShardVos().stream().filter(e -> e.getShardState() == FileState.SUCCESS.getStateCode()).count();
            if (shardSize == count) {
                fileService.updateFileState(fileShardVo.getFileKey(), FileState.SUCCESS);
                // todo 后续引入告警系统，解决万一更新文件状态失败，告警开发人员及时定位
            }
        }
    }
}
