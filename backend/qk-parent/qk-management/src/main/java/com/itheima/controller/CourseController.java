package com.itheima.controller;

import com.itheima.common.PageResult;
import com.itheima.common.Result;
import com.itheima.entity.Course;
import com.itheima.service.impl.CourseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/courses")
@Slf4j
public class CourseController {
    @Autowired
    private CourseServiceImpl courseService;

    @GetMapping
    public Result getPageCourseList(String name, Integer subject, Integer target,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询课程,参数:name={},subject={},target={},page={},pageSize={}",name,subject,target,page,pageSize);
        PageResult<Course> pageResult = courseService.findCourseByPage(name, subject, target, page, pageSize);
        return Result.success(pageResult);
    }

    @DeleteMapping("/{id}")
    public Result deleteCourseById(@PathVariable Integer id){
        log.info("删除课程,id={}",id);
        courseService.deleteCourseById(id);
        return Result.success();
    }

    @PostMapping
    public Result addCourse(@RequestBody Course course){
        log.info("添加课程,参数:{}",course);
        courseService.addCourse(course);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result selectCourseById(@PathVariable Integer id){
        log.info("根据ID查询课程,id={}",id);
        return Result.success(courseService.SelectCourseById(id));
    }

    @PutMapping
    public Result updateCourse(@RequestBody Course course){
        log.info("修改课程,参数:{}",course);
        courseService.updateById(course);
        return Result.success();
    }

    @GetMapping("/list")
    public Result getCourseList(){
        log.info("查询所有课程");
        return Result.success(courseService.CourseList());
    }

    @GetMapping("/subject/{subject}")
    public Result getCourseBySubject(@PathVariable Integer subject){
        log.info("根据学科查询课程,subject={}",subject);
        return Result.success(courseService.selectCourseBySubject(subject));
    }
}
