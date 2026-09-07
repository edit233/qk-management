package com.itheima.controller;

import com.itheima.common.PageResult;
import com.itheima.common.Result;
import com.itheima.entity.Activity;
import com.itheima.request.ActivityDto;
import com.itheima.service.impl.ActivityServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activities")
@Slf4j
public class ActivityController {
    @Autowired
    private ActivityServiceImpl activityService;

    @GetMapping
    public Result getActivityPageList(ActivityDto activityDto) {
        log.info("分页查询活动,参数:channel={},type={}",activityDto.getChannel(),activityDto.getType());
        PageResult<Activity> pageResult = activityService.findActivityByPage(activityDto);
        return Result.success(pageResult);
    }

    @DeleteMapping("/{id}")
    public Result deleteActivity(@PathVariable Integer id) {
        log.info("根据id删除指定活动,参数:id={}",id);
        activityService.removeById(id);
        return Result.success();
    }

    @PostMapping
    public Result addActivity(@RequestBody Activity activity) {
        log.info("添加活动,参数:{}",activity);
        activityService.save(activity);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result selectById(@PathVariable Integer id) {
        log.info("根据id查询活动详情,参数:id={}",id);
        return Result.success(activityService.getById(id));
    }

    @PutMapping
    public Result updateActivity(@RequestBody Activity activity) {
        log.info("修改活动,参数:{}",activity);
        activityService.saveOrUpdate(activity);
        return Result.success();
    }

    @GetMapping("/type/{type}")
    public Result findActivityByType(@PathVariable Integer type) {
        log.info("根据类型查询活动,参数:type={}",type);
        return Result.success(activityService.findActivityByType(type));
    }
}
