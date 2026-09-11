package com.itheima.request;

import lombok.Data;

@Data
public class CluePoolRequest {
    private Integer clueId;
    private String phone;
    private Integer channel;
    private Integer page = 1;
    private Integer pageSize = 10;
}