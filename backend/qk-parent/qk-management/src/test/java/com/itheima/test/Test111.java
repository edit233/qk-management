package com.itheima.test;


import com.itheima.util.OssTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
public class Test111 {
    @Autowired
    private OssTemplate ossTemplate;

    @Test
    public void setOssTemplate() throws FileNotFoundException {
        String filePath = "D:\\Object\\qk-management\\R-C.jpg";
        InputStream inputStream = new FileInputStream(filePath);
        String url = ossTemplate.upload("R-C.jpg", inputStream);
        System.out.println(url);
    }



}
