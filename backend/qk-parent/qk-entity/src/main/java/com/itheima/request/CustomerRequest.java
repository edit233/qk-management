package com.itheima.request;

import lombok.Data;

@Data
public class CustomerRequest {
    private String name;
    private String phone;
    private Integer channel;
    private Integer subject;
    private Integer page = 1;
    private Integer pageSize = 10;
}