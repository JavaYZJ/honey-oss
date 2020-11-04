package com.eboy.honey.oss.server.api.web.controller;

import com.eboy.honey.oss.api.entiy.Thumbnail;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.utils.HoneyIOUtil;
import com.eboy.honey.response.commmon.HoneyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;

/**
 * @author yangzhijie
 * @date 2020/10/14 14:11
 */
@RestController
@RequestMapping("/v1/upload")
public class UploadController {

    @Autowired
    private FileService fileService;

    /**
     * 通用上传
     *
     * @param file       文件
     * @param bucketName 桶名
     */
    @PostMapping("/sync/{bucketName}")
    public HoneyResponse<String> upload(MultipartFile file, @PathVariable String bucketName) {
        File upload = HoneyIOUtil.multipartFile2File(file);
        String fileKey = fileService.upload(upload, bucketName, MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())));
        return HoneyResponse.success(fileKey);
    }

    /**
     * 通用异步上传
     *
     * @param file        文件
     * @param bucketName  桶名
     * @param callbackUrl 回调接口url
     */
    @PostMapping("/async/{bucketName}")
    public HoneyResponse asyncUpload(MultipartFile file, @PathVariable String bucketName, String callbackUrl) {
        File upload = HoneyIOUtil.multipartFile2File(file);
        fileService.asyncUpload(upload, bucketName, MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())), callbackUrl);
        return HoneyResponse.success();
    }

    /**
     * 图片上传
     *
     * @param file          图片
     * @param bucketName    桶名
     * @param needThumbnail 是否需要缩略图(需要时，则为默认的缩略图)
     */
    @PostMapping("/image/{bucketName}")
    public HoneyResponse<String> image(MultipartFile file, @PathVariable String bucketName,
                                       @RequestParam(defaultValue = "false") boolean needThumbnail) {
        File upload = HoneyIOUtil.multipartFile2File(file);
        String image = fileService.uploadImage(upload, bucketName, MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())), needThumbnail);
        return HoneyResponse.success(image);
    }

    /**
     * 有缩略图构建规则的图片上传
     *
     * @param file       图片
     * @param bucketName 桶名
     * @param thumbnail  缩略图构建规则
     */
    @PutMapping("/image/{bucketName}")
    public HoneyResponse<String> image(MultipartFile file, @PathVariable String bucketName, @RequestBody Thumbnail thumbnail) {
        File upload = HoneyIOUtil.multipartFile2File(file);
        String image = fileService.uploadImage(upload, bucketName, MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())), thumbnail);
        return HoneyResponse.success(image);
    }

}
