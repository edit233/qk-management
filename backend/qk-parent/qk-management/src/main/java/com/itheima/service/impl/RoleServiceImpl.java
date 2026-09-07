package com.itheima.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.PageResult;
import com.itheima.entity.Role;
import com.itheima.mapper.RoleMapper;
import com.itheima.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper,Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper ;

    @Override
    public PageResult<Role> findRoleByPage(String name, String label, Integer page, Integer pageSize) {
        Page<Role> rolePage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), Role::getName, name)
                .like(StrUtil.isNotBlank(label), Role::getLabel, label);
        rolePage = roleMapper.selectPage(rolePage, wrapper);
        return new PageResult<>(rolePage.getTotal(), rolePage.getRecords());
    }


}
