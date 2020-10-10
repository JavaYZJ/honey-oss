package com.eboy.honey.oss.server.api.web.controller;

import com.eboy.honey.oss.server.application.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @author yangzhijie
 * @date 2020/10/10 14:34
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${honey.oss.minio.bucketName}")
    private String bucketName;
    @Autowired
    private FileService fileService;

    @GetMapping("/callback")
    public void testCallback() {
        String pathName = "C:\\Users\\admin\\Pictures\\Saved Pictures\\6.jpg";
        File file = new File(pathName);
        fileService.asyncUpload(file, bucketName, MediaType.IMAGE_JPEG, "http://localhost:8089/rest/callback");
    }
}
