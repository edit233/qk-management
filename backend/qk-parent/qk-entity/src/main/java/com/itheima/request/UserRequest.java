package com.itheima.request;

import lombok.Data;

@Data
public class UserRequest {
    private String name; // 用户名
    private Integer status; // 状态：0-禁用，1-启用
    private String phone; // 手机号
    private Long deptId; // 部门ID
    private Integer page = 1;
    private Integer pageSize = 10;
}
