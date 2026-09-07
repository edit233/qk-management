package com.itheima.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.PageResult;
import com.itheima.entity.Activity;
import com.itheima.request.ActivityDto;

import java.util.List;

public interface ActivityService extends IService<Activity> {
    PageResult<Activity> findActivityByPage(ActivityDto activityDto);

    List<Activity> findActivityByType(Integer type);
}
