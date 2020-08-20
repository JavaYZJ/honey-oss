package com.eboy.honey.oss.server.application.componet;

import com.eboy.honey.oss.constant.FileState;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.service.FileShardService;
import com.eboy.honey.oss.server.application.vo.FileShardVo;
import com.eboy.honey.oss.server.application.vo.FileVo;
import com.eboy.honey.oss.server.client.HoneyMiniO;
import com.eboy.honey.oss.utils.HoneyFileUtil;
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
public class AsyncTask {

    @Autowired
    private HoneyMiniO honeyMiniO;

    /**
     * 异步上传
     */
    @Async
    public void asyncUpload(FileShardService service, FileShardVo fileShardVo, String bucketName, MediaType contentType) {
        // 上传至Minio
        String objectName = HoneyFileUtil.buildShardObjectName(fileShardVo.getShardName(), fileShardVo.getShardIndex());
        honeyMiniO.upload(bucketName, fileShardVo.getFileKey(), fileShardVo.getHoneyStream().getInputStream(), contentType);
        // 上传成功后，修改该分片状态为 成功
        service.updateFileShardState(fileShardVo.getUid(), FileState.SUCCESS);
    }

    /**
     * 异步上传
     */
    @Async
    public void asyncUpload(FileShardService service, FileVo fileVo, String bucketName, MediaType contentType) {
        // 上传至Minio
        String objectName = HoneyFileUtil.buildObjectNameByFileKey(fileVo.getFileName(), fileVo.getFileKey());
        honeyMiniO.upload(bucketName, objectName, fileVo.getHoneyStream().getInputStream(), contentType);
        // 上传成功后，修改该分片状态为 成功
        service.updateFileShardState(fileVo.getUid(), FileState.SUCCESS);
    }

    /**
     * 异步检查是不是最后一块并修改文件的状态为成功
     *
     * @deprecated
     */
    @Async
    public void asyncCheckAndMerge(FileService service, FileShardVo fileShardVo) {
        List<FileVo> fileInfos = service.getFileByFileKeys(Collections.singletonList(fileShardVo.getFileKey()));
        if (!CollectionUtils.isEmpty(fileInfos)) {
            FileVo fileVo = fileInfos.get(0);
            // 文件总分片数
            int shardSize = fileVo.getShardTotal();
            // 该文件的分片已经处于成功的数量
            long count = fileVo.getFileShardVos().stream().filter(e -> e.getShardState() == FileState.SUCCESS.getStateCode()).count();
            if (shardSize == count) {
                service.updateFileState(fileShardVo.getFileKey(), FileState.SUCCESS);
                // todo 后续引入告警系统，解决万一更新文件状态失败，告警开发人员及时定位
            }
        }
    }
}
