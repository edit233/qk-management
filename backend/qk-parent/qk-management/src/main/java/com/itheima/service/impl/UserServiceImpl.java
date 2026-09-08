package com.itheima.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.PageResult;
import com.itheima.entity.User;
import com.itheima.mapper.UserMapper;
import com.itheima.request.UserDto;
import com.itheima.response.LoginResponse;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    public PageResult<User> getUsers(UserDto userDto) {
        // 1.设置分页条件
        Page<User> p = new Page<>(userDto.getPage(), userDto.getPageSize());

        // 2. 执行分页查询
        IPage<User> userPage = userMapper.getUsers(p, userDto);

        // 3. 封装返回结果
        return new PageResult<>(userPage.getTotal(), userPage.getRecords());
    }

    @Override
    public List<User> selectUserByRoleLabel(String roleLabel) {
        return userMapper.selectUserByRoleLabel(roleLabel);
    }

    @Override
    public List<User> selectUserByDeptId(Integer deptId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(deptId != null, User::getDeptId, deptId);
        return userMapper.selectList(wrapper);
    }

    @Override
    public LoginResponse login(String username, String password) {
        LoginResponse loginUser = userMapper.selectByUserName(username);

        if (loginUser == null) {
            throw new RuntimeException("用户名错误");
        }

        if (loginUser.getStatus() == 0){
            throw new RuntimeException("用户被冻结");
        }

        if (loginUser.getPassword().equals(DigestUtil.md5Hex(password))){
            throw new RuntimeException("密码错误");
        }

        loginUser.setToken("");
        loginUser.setPassword(null);
        return loginUser;
    }
}
