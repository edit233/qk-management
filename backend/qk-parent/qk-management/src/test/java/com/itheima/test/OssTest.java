package com.itheima.test;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 手动验证 OSS 上传（不参与 CI）。
 * 只有当环境变量 OSS_ACCESS_KEY_ID 存在时才会执行；
 * CI 环境没有该变量，自动跳过。
 * 本地运行：先设置 OSS_ACCESS_KEY_ID / OSS_ACCESS_KEY_SECRET 环境变量。
 */
@EnabledIfEnvironmentVariable(named = "OSS_ACCESS_KEY_ID", matches = ".+")
public class OssTest {

    @Test
    public void testUpload() throws FileNotFoundException {
        // 区域
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";

        // 密钥对（从环境变量读取，不硬编码）
        String accessKeyId = System.getenv("OSS_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("OSS_ACCESS_KEY_SECRET");

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件（默认在工作目录找，不再写死绝对路径）
        InputStream inputStream = new FileInputStream("R-C.jpg");

        //参数1: 桶名字  参数2: 图片上传后名字   参数3: 图片流
        ossClient.putObject("giao233", "haha.jpg", inputStream);

        // 关闭OSSClient
        ossClient.shutdown();
    }
}