package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.entity.User;
import com.itheima.request.UserRequest;
import com.itheima.response.LoginResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    IPage<User> getUsers(Page<User> page,@Param("userRequest") UserRequest userRequest);

    List<User> selectUserByRoleLabel(String roleLabel);

    List<User> selectUserByDeptId(Integer deptId);

    LoginResponse selectByUserName(String username);
}
