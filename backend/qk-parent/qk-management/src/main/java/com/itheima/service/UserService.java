package com.itheima.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.PageResult;
import com.itheima.request.UserDto;
import com.itheima.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    /**
     * 条件分页查询用户列表
     *
     * @param userDto 查询参数
     * @return 查询结果
     */
    PageResult<User> getUsers(UserDto userDto);

    List<User> selectUserByRoleLabel(String roleLabel);

    List<User> selectUserByDeptId(Integer deptId);
}
