package com.itheima.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.PageResult;
import com.itheima.entity.Course;
import com.itheima.mapper.CourseMapper;
import com.itheima.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public PageResult<Course> findCourseByPage(String name, Integer subject, Integer target, Integer page, Integer pageSize) {
        LambdaQueryWrapper<Course> courseWrapper = new LambdaQueryWrapper<>();
        courseWrapper
                .like(StrUtil.isNotBlank(name),Course::getName,name)
                .eq(subject != null,Course::getSubject,subject)
                .eq(target != null,Course::getTarget,target);
        Page<Course> coursePage = courseMapper.selectPage(new Page<>(page,pageSize) ,courseWrapper);

        return new PageResult<>(coursePage.getTotal(),coursePage.getRecords());
    }

    @Override
    public void deleteCourseById(Integer id) {
        courseMapper.deleteById(id);
    }

    @Override
    public void addCourse(Course course) {
        courseMapper.insert(course);
    }

    @Override
    public Course SelectCourseById(Integer id) {
        return courseMapper.selectById(id);
    }

    @Override
    public void updateById(Course course) {
        course.setUpdateTime(LocalDateTime.now());
        courseMapper.updateById(course);
    }

    @Override
    public List<Course> CourseList() {
        return courseMapper.selectList(null);
    }

    @Override
    public List<Course> selectCourseBySubject(Integer subject) {
        LambdaQueryWrapper<Course> courseWrapper = new LambdaQueryWrapper<>();
        courseWrapper.eq(subject != null,Course::getSubject,subject);
        return courseMapper.selectList(courseWrapper);
    }
}
