package com.itheima.service;

import com.itheima.common.PageResult;
import com.itheima.entity.Dept;

import java.util.List;

public interface DeptService {

    void addDept(Dept dept);

    /**
     * 分页查询部门
     *
     * @param name     部门名称
     * @param status   部门状态
     * @param page     当前页码
     * @param pageSize 每页显示条数
     * @return 分页结果
     */
    PageResult<Dept> findDeptsByPage(String name, Integer status, Integer page, Integer pageSize);

    /**
     * 根据id查询部门
     *
     * @param id 部门id
     * @return 部门信息
     */
    Dept findById(Integer id);

    /**
     * 根据id修改部门信息
     *
     * @param dept 部门信息
     */
    void updateById(Dept dept);

    /**
     * 根据id删除部门
     *
     * @param id 部门id
     */
    void deleteById(Integer id);

    List<Dept> deptList();
}

