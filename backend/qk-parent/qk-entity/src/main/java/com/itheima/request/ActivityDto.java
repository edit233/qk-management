package com.itheima.request;

import lombok.Data;

@Data
public class ActivityDto {
    private Integer channel;
    private Integer type;
    private Integer page = 1;
    private Integer pageSize = 10;


}
