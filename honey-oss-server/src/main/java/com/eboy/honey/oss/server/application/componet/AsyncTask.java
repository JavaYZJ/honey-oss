package com.eboy.honey.oss.server.application.componet;

import com.eboy.honey.oss.constant.FileState;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.service.FileShardService;
import com.eboy.honey.oss.server.application.vo.FileShardVo;
import com.eboy.honey.oss.server.application.vo.FileVo;
import com.eboy.honey.oss.server.client.HoneyMiniO;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private FileShardService fileShardService;

    @Autowired
    private FileService fileService;

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
     * 异步检查是不是最后一块并修改文件的状态为成功
     */
    @Async
    public void asyncCheckAndMerge(FileShardVo fileShardVo) {
        List<FileVo> fileInfos = fileService.getFileByFileKeys(Collections.singletonList(fileShardVo.getFileKey()));
        if (!CollectionUtils.isEmpty(fileInfos)) {
            FileVo fileVo = fileInfos.get(0);
            // 文件总分片数
            int shardSize = fileVo.getShardSize();
            // 该文件的分片已经处于成功的数量
            long count = fileVo.getFileShardVos().stream().filter(e -> e.getShardState() == FileState.SUCCESS.getStateCode()).count();
            if (shardSize == count) {
                fileService.updateFileState(fileShardVo.getFileKey(), FileState.SUCCESS);
                // todo 后续引入告警系统，解决万一更新文件状态失败，告警开发人员及时定位
            }
        }
    }
}
