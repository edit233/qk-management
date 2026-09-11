package com.itheima.request;

import lombok.Data;

@Data
public class BusinessesRequest {
    private Integer businessId;
    private String name;
    private String phone;
    private Integer status;
    private String assignName;
    private Integer page = 1;
    private Integer pageSize = 10;

}
