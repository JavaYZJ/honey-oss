package com.eboy.honey.oss.server.api.web.controller;

import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.service.ThumbnailService;
import com.eboy.honey.oss.server.application.utils.HoneyIOUtil;
import com.eboy.honey.oss.utils.HoneyFileUtil;
import com.eboy.honey.response.commmon.HoneyResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

/**
 * @author yangzhijie
 * @date 2020/10/15 9:36
 */
@RestController
@RequestMapping("/v1/down")
public class DownController {

    @Autowired
    private FileService fileService;
    @Autowired
    private ThumbnailService thumbnailService;

    /**
     * 下载(url)
     *
     * @param fileKey    文件key
     * @param bucketName 桶名
     * @param expires    过期时间
     * @return url
     */
    @GetMapping("/url/{bucketName}/{fileKey}")
    public HoneyResponse<String> downAsUrl(@PathVariable String fileKey, @PathVariable String bucketName,
                                           @RequestParam(required = false, defaultValue = "604800") Integer expires) {
        return HoneyResponse.success(fileService.downAsUrl(fileKey, bucketName, expires));
    }

    /**
     * 下载缩略图(url)
     *
     * @param fileKey    文件key
     * @param bucketName 桶名
     * @param expires    过期时间
     * @return url
     */
    @GetMapping("/url/thumbnail/{bucketName}/{fileKey}")
    public HoneyResponse<String> downThumbnailByOriginal(@PathVariable String fileKey, @PathVariable String bucketName,
                                                         @RequestParam(required = false, defaultValue = "604800") Integer expires) {
        return HoneyResponse.success(thumbnailService.getUrlByOriginalPicture(bucketName, fileKey, expires));
    }

    /**
     * 下载(流)
     *
     * @param fileKey    文件key
     * @param bucketName 桶名
     * @return stream
     */
    @SneakyThrows
    @GetMapping("/stream/{bucketName}/{fileKey}")
    public ResponseEntity<InputStream> downAsStream(@PathVariable String fileKey, @PathVariable String bucketName) {
        InputStream inputStream = fileService.downAsStream(bucketName, fileKey);
        return HoneyIOUtil.giveBackStream(inputStream, HoneyFileUtil.get32Uid() + ".jpg");
    }

    /**
     * 下载(流)
     *
     * @param fileKey    文件key
     * @param bucketName 桶名
     * @return stream
     */
    @SneakyThrows
    @GetMapping("/stream/thumbnail/{bucketName}/{fileKey}")
    public ResponseEntity<InputStream> downThumbnailByOriginal(@PathVariable String fileKey, @PathVariable String bucketName) {
        InputStream inputStream = thumbnailService.getStreamByOriginalPicture(bucketName, fileKey);
        return HoneyIOUtil.giveBackStream(inputStream, HoneyFileUtil.get32Uid() + ".jpg");
    }
}
