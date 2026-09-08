package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.entity.User;
import com.itheima.request.UserDto;
import com.itheima.response.LoginResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    IPage<User> getUsers(Page<User> page, UserDto userDto);

    List<User> selectUserByRoleLabel(String roleLabel);

    List<User> selectUserByDeptId(Integer deptId);

    LoginResponse selectByUserName(String username);
}
