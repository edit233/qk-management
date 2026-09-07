package com.itheima.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.PageResult;
import com.itheima.entity.Dept;
import com.itheima.mapper.DeptMapper;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public void addDept(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());

        deptMapper.insert(dept);
    }

    @Override
    public PageResult<Dept> findDeptsByPage(String name, Integer status, Integer page, Integer pageSize) {
        //分页插件使用步骤
        //1.设置分页参数
        Page<Dept> p = new Page<>(page, pageSize);

        //2. 设置条件参数
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), Dept::getName, name)
                .eq(status != null, Dept::getStatus, status);

        //3. 执行分页查询
        p = deptMapper.selectPage(p,wrapper);

        //4. 返回结果
        return new PageResult<>(p.getTotal(), p.getRecords());
    }

    @Override
    public Dept findById(Integer id) {
        return deptMapper.selectById(id);
    }

    @Override
    public void updateById(Dept dept) {
        //补齐修改时间
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.updateById(dept);
    }

    @Override
    public void deleteById(Integer id) {
        deptMapper.deleteById(id);
    }

    @Override
    public List<Dept> deptList() {
        return deptMapper.selectList(null);
    }
}
