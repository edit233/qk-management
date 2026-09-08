package com.itheima.controller;

import com.itheima.common.Result;
import com.itheima.util.OssTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    private OssTemplate ossTemplate;

    /**
     * 文件上传
     *
     * @param image 上传的图片
     * @return 文件上传的url
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile image) throws IOException {
        log.info("文件上传开始：{}", image.getOriginalFilename());
        String url = ossTemplate.upload(image.getOriginalFilename(), image.getInputStream());
        log.info("文件上传完成：{}", url);
        return Result.success(url);
    }
}