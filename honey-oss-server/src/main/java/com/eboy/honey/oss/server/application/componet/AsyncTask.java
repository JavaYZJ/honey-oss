package com.eboy.honey.oss.server.application.componet;

import com.eboy.honey.oss.constant.FileState;
import com.eboy.honey.oss.server.application.service.FileShardService;
import com.eboy.honey.oss.server.application.vo.FileShardVo;
import com.eboy.honey.oss.server.client.HoneyMiniO;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author yangzhijie
 * @date 2020/8/12 16:42
 */
@Component
public class AsyncTask {

    @Value("${honey.oss.minio.bucketName}")
    private String bucketName;

    @Autowired
    private HoneyMiniO honeyMiniO;

    @Autowired
    private FileShardService fileShardService;

    /**
     * 异步上传
     */
    @Async
    public void asyncUpload(FileShardVo fileShardVo, String bucketName, ContentType contentType) {
        // 上传至Minio
        honeyMiniO.upload(bucketName, fileShardVo.getShardName(), fileShardVo.getFileShardStream(), contentType);
        // 上传成功后，修改该分片状态为 成功
        fileShardService.updateFileShardState(fileShardVo.getUid(), FileState.SUCCESS);
    }

    /**
     * 异步上传
     */
    @Async
    public void asyncUpload(FileShardVo fileShardVo, ContentType contentType) {
        // 上传至Minio
        honeyMiniO.upload(bucketName, fileShardVo.getShardName(), fileShardVo.getFileShardStream(), contentType);
        // 上传成功后，修改该分片状态为 成功
        fileShardService.updateFileShardState(fileShardVo.getUid(), FileState.SUCCESS);
    }

    /**
     * 异步检查是不是最后一块并合并文件
     */
    @Async
    public void asyncCheckAndMerge(FileShardVo fileShardVo) {
        
    }
}
