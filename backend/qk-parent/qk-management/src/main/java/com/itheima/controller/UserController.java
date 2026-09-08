package com.itheima.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.itheima.common.PageResult;
import com.itheima.common.Result;
import com.itheima.entity.User;
import com.itheima.request.UserDto;
import com.itheima.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    /**
     * 条件分页查询用户列表
     */
    @GetMapping
    public Result getUsers(UserDto userDto) {
        log.info("分页查询用户,参数:name={},phone={},deptId={},status={},page={},pageSize={}", userDto.getName(), userDto.getPhone(), userDto.getDeptId(), userDto.getStatus(), userDto.getPage(), userDto.getPageSize());
        PageResult<User> userPage = userService.getUsers(userDto);
        return Result.success(userPage);
    }

    @DeleteMapping("/{ids}")
    public Result deleteUser(@PathVariable List<User> ids) {
        log.info("批量删除用户,参数:ids={}", ids);
        boolean b = userService.removeBatchByIds(ids);
        return b ? Result.success() : Result.error("删除失败");
    }

    @PostMapping
    public Result addUser(@RequestBody User user) {
        log.info("添加用户,参数:username={},name={},phone={}", user.getUsername(), user.getName(), user.getPhone());
        user.setPassword(DigestUtil.md5Hex(user.getUsername() + "123"));
        boolean b = userService.save(user);
        return b ? Result.success() : Result.error("添加失败");
    }

    @GetMapping("/{id}")
    public Result selectUserById(@PathVariable Integer id) {
        log.info("根据id查询用户详情,参数:id={}", id);
        User user = userService.getById(id);
        return Result.success(user);
    }

    @PutMapping()
    public Result updateUser(@RequestBody User user) {
        log.info("修改用户信息,参数:user={}", user);
        boolean b = userService.saveOrUpdate(user);
        return b ? Result.success() : Result.error("修改失败");
    }

    @GetMapping("/role/{roleLabel}")
    public Result getUserByRoleLabel(@PathVariable String roleLabel) {
        log.info("根据角色查询用户,参数:roleLabel={}", roleLabel);
        return Result.success(userService.selectUserByRoleLabel(roleLabel));
    }

    @GetMapping("/dept/{deptId}")
    public Result getUserByDeptId(@PathVariable Integer deptId) {
        log.info("根据部门查询用户,参数:deptId={}", deptId);
        return Result.success(userService.selectUserByDeptId(deptId));
    }
}
