package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.PageResult;
import com.itheima.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    PageResult<Role> findRoleByPage(String name, String label, Integer page, Integer pageSize);

}
