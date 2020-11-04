package com.eboy.honey.oss.api;

import com.eboy.honey.oss.api.service.FileRpcService;
import com.eboy.honey.oss.api.service.FileShardRpcService;
import com.eboy.honey.oss.client.HoneyMiniO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yangzhijie
 * @date 2020/11/4 10:41
 */
@Slf4j
public class HoneyOss {

    /**
     * 来自honey-oss-server的文件服务
     */
    @Reference(version = "1.0")
    private FileRpcService fileRpcService;
    /**
     * 来自honey-oss-server的文件分片服务
     */
    @Reference(version = "1.0")
    private FileShardRpcService fileShardRpcService;

    @Autowired
    private HoneyMiniO honeyMiniO;


    public String downAsUrl(String bucketName, String fileKey) {
        return fileRpcService.downAsUrl(bucketName, fileKey);
    }


}
