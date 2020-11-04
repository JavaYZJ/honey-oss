package com.eboy.honey.oss.server.application.utils;


import com.eboy.honey.oss.api.dto.HoneyStream;
import com.eboy.honey.oss.server.application.vo.FileShardVo;
import com.eboy.honey.oss.server.application.vo.FileVo;
import org.apache.http.util.Asserts;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author yangzhijie
 * @date 2020/8/17 14:00
 */
public class ArgsCheckUtil {

    /**
     * 文件分片参数校验
     *
     * @param fileShard 文件分片
     */
    public static void checkFileShard(FileShardVo fileShard) {
        String uid = fileShard.getUid();
        Asserts.notNull(uid, "FileShard uid");
        String fileKey = fileShard.getFileKey();
        Asserts.notNull(fileKey, "FileShard fileKey");
        String shardName = fileShard.getShardName();
        Asserts.notNull(shardName, "FileShard shardName");
        int shardIndex = fileShard.getShardIndex();
        Asserts.check(shardIndex == 0, "请指定当前分片索引");
        HoneyStream honeyStream = fileShard.getHoneyStream();
        Asserts.notNull(honeyStream, "FileShard honeyStream");
    }

    /**
     * 文件参数校验
     *
     * @param fileVo 文件
     */
    public static void checkFile(FileVo fileVo, boolean isShard) {
        String uid = fileVo.getUid();
        Asserts.notNull(uid, "File uid");
        String fileKey = fileVo.getFileKey();
        Asserts.notNull(fileKey, "File fileKey");
        String fileName = fileVo.getFileName();
        Asserts.notNull(fileName, "File fileName");
        String fileSuffix = fileVo.getFileSuffix();
        Asserts.notNull(fileSuffix, "File fileSuffix");
        if (isShard) {
            List<FileShardVo> fileShardVos = fileVo.getFileShardVos();
            Asserts.check(!CollectionUtils.isEmpty(fileShardVos), "分片上传时，分片的文件流不能为空");
        } else {
            HoneyStream honeyStream = fileVo.getHoneyStream();
            Asserts.notNull(honeyStream, "File honeyStream");
        }
    }
}
