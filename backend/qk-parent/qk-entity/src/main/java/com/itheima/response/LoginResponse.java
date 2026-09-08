package com.itheima.response;

import lombok.Data;

/**
 * 登录结果实体类
 * 用于封装登录后返回给前端的结果
 */
@Data
public class LoginResponse {
    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像URL
     */
    private String image;

    /**
     * 角色标签
     */
    private String roleLabel;

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态
     */
    private Integer status;
}