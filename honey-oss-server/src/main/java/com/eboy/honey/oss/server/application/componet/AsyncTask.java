package com.eboy.honey.oss.server.application.componet;

import com.eboy.honey.oss.constant.FileState;
import com.eboy.honey.oss.entiy.CallBack;
import com.eboy.honey.oss.server.application.service.CallBackService;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.service.FileShardService;
import com.eboy.honey.oss.server.application.utils.CallBackUtil;
import com.eboy.honey.oss.server.application.vo.FileShardVo;
import com.eboy.honey.oss.server.application.vo.FileVo;
import com.eboy.honey.oss.server.client.HoneyMiniO;
import com.eboy.honey.oss.utils.HoneyFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CallBackService<String> callBackService;

    /**
     * 分片异步上传
     */
    @Async
    public void asyncUpload(FileShardVo fileShardVo, String bucketName, MediaType contentType, String callbackUrl) {
        String msg = "success";
        String data = fileShardVo.getFileKey();
        int code = 200;
        try {
            // 上传至Minio
            String objectName = HoneyFileUtil.buildObjectNameByFileKey(fileShardVo.getShardName(), fileShardVo.getFileKey());
            honeyMiniO.upload(bucketName, objectName, fileShardVo.getHoneyStream().getInputStream(), contentType);
            // 上传成功后，修改该分片状态为 成功
            fileShardService.updateFileShardState(fileShardVo.getUid(), FileState.SUCCESS);
        } catch (Exception e) {
            code = 500;
            msg = e.getMessage();
            data = null;
        }
        // 回调
        CallBack<String> callBack = CallBackUtil.buildCallback(data, callbackUrl, code, msg);
        callBackService.callBack(callBack);
    }

    /**
     * 异步上传
     */
    @Async
    public void asyncUpload(FileVo fileVo, String bucketName, MediaType contentType, String callbackUrl) {
        String msg = "success";
        String data = fileVo.getFileKey();
        int code = 200;
        try {
            // 上传至Minio
            String objectName = HoneyFileUtil.buildObjectNameByFileKey(fileVo.getFileName(), fileVo.getFileKey());
            honeyMiniO.upload(bucketName, objectName, fileVo.getHoneyStream().getInputStream(), contentType);
            // 上传成功后，修改该文件状态为 成功
            fileService.updateFileState(fileVo.getFileKey(), FileState.SUCCESS);
        } catch (Exception e) {
            code = 500;
            msg = e.getMessage();
            data = null;
        } finally {
            // 回调
            CallBack<String> callBack = CallBackUtil.buildCallback(data, callbackUrl, code, msg);
            callBackService.callBack(callBack);
        }
    }

    /**
     * 异步检查是不是最后一块并修改文件的状态为成功
     *
     * @deprecated
     */
    @Async
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
