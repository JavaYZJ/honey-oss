package com.eboy.honey.oss.server.api.web.controller;

import com.eboy.honey.oss.entiy.Thumbnail;
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

    @PostMapping("/sync")
    public HoneyResponse<String> upload(MultipartFile file, String bucketName) {
        File upload = HoneyIOUtil.multipartFile2File(file);
        String fileKey = fileService.upload(upload, bucketName, MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())));
        return HoneyResponse.success(fileKey);
    }

    @PostMapping("/async")
    public HoneyResponse asyncUpload(MultipartFile file, String bucketName, String callbackUrl) {
        File upload = HoneyIOUtil.multipartFile2File(file);
        fileService.asyncUpload(upload, bucketName, MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())), callbackUrl);
        return HoneyResponse.success();
    }

    @PostMapping("/image")
    public HoneyResponse<String> image(MultipartFile file, String bucketName,
                                       @RequestParam(defaultValue = "false") boolean needThumbnail) {
        File upload = HoneyIOUtil.multipartFile2File(file);
        String image = fileService.uploadImage(upload, bucketName, MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())), needThumbnail);
        return HoneyResponse.success(image);
    }

    @PostMapping("/thumbnail")
    public HoneyResponse<String> thumbnail(MultipartFile file, String bucketName, @RequestBody Thumbnail thumbnail) {
        File upload = HoneyIOUtil.multipartFile2File(file);
        String image = fileService.uploadImage(upload, bucketName, MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())), thumbnail);
        return HoneyResponse.success(image);
    }
}
