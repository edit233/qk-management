package com.itheima.controller;

import com.itheima.common.Result;
import com.itheima.entity.User;
import com.itheima.response.LoginResponse;
import com.itheima.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param user 账号和密码
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        log.info("用户登录请求: {}", user);
        LoginResponse loginResult = userService.login(user.getUsername(), user.getPassword());
        log.info("用户登录结果: {}", loginResult);
        return Result.success(loginResult);

    }
}