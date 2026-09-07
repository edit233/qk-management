package com.itheima.service;


import com.itheima.common.PageResult;
import com.itheima.entity.Course;

import java.util.List;

public interface CourseService {
    PageResult<Course> findCourseByPage(String name, Integer subject,Integer target, Integer page, Integer pageSize);

    void  deleteCourseById(Integer id);

    void addCourse(Course course);

    Course SelectCourseById(Integer id);

    void updateById(Course course);

    List<Course> CourseList();

    List<Course> selectCourseBySubject(Integer subject);
}
