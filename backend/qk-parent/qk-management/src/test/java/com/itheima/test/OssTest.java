package com.itheima.test;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class OssTest {

    @Test
    public void testUpload() throws FileNotFoundException {
        // 区域
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";

        // 密钥对
        String accessKeyId = "YOUR_ACCESS_KEY_ID";
        String accessKeySecret = "YOUR_ACCESS_KEY_SECRET";

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件
        InputStream inputStream = new FileInputStream("D:\\Object\\qk-management\\R-C.jpg");

        //参数1: 桶名字  参数2: 图片上传后名字   参数3: 图片流
        ossClient.putObject("giao233", "haha.jpg", inputStream);

        // 关闭OSSClient
        ossClient.shutdown();
    }
}