package com.itheima.request;

import lombok.Data;

@Data
public class LogRequest {
    private String operateUserName;
    private Integer page = 1;
    private Integer pageSize = 10;
}
