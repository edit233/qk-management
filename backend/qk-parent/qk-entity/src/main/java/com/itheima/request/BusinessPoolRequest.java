package com.itheima.request;

import lombok.Data;

@Data
public class BusinessPoolRequest {
    private Integer businessId;
    private Integer phone;
    private String name;
    private Integer subject;
    private Integer page;
    private Integer pageSize;
}
