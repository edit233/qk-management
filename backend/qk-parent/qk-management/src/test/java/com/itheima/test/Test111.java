package com.itheima.test;


import com.itheima.util.OssTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 手动验证 OssTemplate 上传（不参与 CI）。
 * 需要 OSS_ACCESS_KEY_ID 环境变量才会执行（CI 上自动跳过）；
 * 同时 @SpringBootTest 需要可连接的数据库，仅适合本地运行。
 */
@EnabledIfEnvironmentVariable(named = "OSS_ACCESS_KEY_ID", matches = ".+")
@SpringBootTest
public class Test111 {
    @Autowired
    private OssTemplate ossTemplate;

    @Test
    public void setOssTemplate() throws FileNotFoundException {
        String filePath = "R-C.jpg";
        InputStream inputStream = new FileInputStream(filePath);
        String url = ossTemplate.upload("R-C.jpg", inputStream);
        System.out.println(url);
    }




}