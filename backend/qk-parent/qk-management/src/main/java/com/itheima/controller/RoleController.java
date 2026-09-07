package com.itheima.controller;

import com.itheima.common.PageResult;
import com.itheima.common.Result;
import com.itheima.entity.Role;
import com.itheima.service.impl.RoleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@Slf4j
public class RoleController {
    @Autowired
    private RoleServiceImpl roleService;

    @GetMapping
    public Result listRole(String name, String label,
                           @RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询角色,参数:name={},label={},page={},pageSize={}",name,label,page,pageSize);
        PageResult<Role> pageResult = roleService.findRoleByPage(name, label, page, pageSize);
        return Result.success(pageResult);
    }

    @DeleteMapping("/{id}")
    public Result deleteRoleById(@PathVariable Integer id){
        log.info("删除角色,id={}",id);
        roleService.removeById(id);
        return Result.success();
    }

    @PostMapping
    public Result addRole(@RequestBody Role role){
        log.info("添加角色,参数:{}",role);
        roleService.save(role);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result selectRoleById(@PathVariable Integer id){
        log.info("根据ID查询角色,id={}",id);
        return Result.success(roleService.getById(id));
    }

    @PutMapping
    public Result updateRole(@RequestBody Role role){
        log.info("修改角色,参数:{}",role);
        roleService.updateById(role);
        return Result.success();
    }

    @GetMapping("/list")
    public Result getRoleList(){
        log.info("查询所有角色");
        return Result.success(roleService.list());
    }
}
