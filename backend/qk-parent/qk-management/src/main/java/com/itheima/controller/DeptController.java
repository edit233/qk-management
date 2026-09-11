package com.itheima.controller;

import com.itheima.aspect.anno.LogOperation;
import com.itheima.common.PageResult;
import com.itheima.common.Result;
import com.itheima.entity.Dept;
import com.itheima.service.impl.DeptServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depts")
@Slf4j
public class DeptController {
    @Autowired
    private DeptServiceImpl deptService;

    @PostMapping
    public Result addDept(@RequestBody Dept dept){
        log.info("添加部门,参数:{}",dept);
        deptService.addDept(dept);
        return Result.success();
    }

    /**
     * 条件分页查询部门
     *
     * @param name     部门名称
     * @param status   状态
     * @param page     页码
     * @param pageSize 每页记录数
     * @return 分页查询结果
     */
    @LogOperation
    @GetMapping
    public Result listDepts(String name, Integer status,
                            @RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询部门,参数:name={},status={},page={},pageSize={}",name,status,page,pageSize);
        PageResult<Dept> pageResult = deptService.findDeptsByPage(name, status, page, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询部门
     *
     * @param id 部门ID
     * @return 查询结果
     */
    @LogOperation
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        log.info("根据ID查询部门,id={}",id);
        Dept dept = deptService.findById(id);
        return Result.success(dept);
    }

    /**
     * 修改部门
     *
     * @param dept 部门信息
     * @return 统一响应结果
     */
    @LogOperation
    @PutMapping
    public Result updateDept(@RequestBody Dept dept) {
        log.info("修改部门,参数:{}",dept);
        deptService.updateById(dept);
        return Result.success();
    }

    /**
     * 删除部门
     *
     * @param id 部门ID
     * @return 统一响应结果
     */
    @LogOperation
    @DeleteMapping("/{id}")
    public Result deleteDept(@PathVariable Integer id) {
        log.info("删除部门,id={}",id);
        deptService.deleteById(id);
        return Result.success();
    }
    @LogOperation
    @GetMapping("/list")
    public Result allDeptList(){
        log.info("查询所有部门");
        List<Dept> deptList = deptService.deptList();
        return Result.success(deptList);
    }
}
