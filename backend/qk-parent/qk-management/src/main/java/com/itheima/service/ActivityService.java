package com.itheima.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.PageResult;
import com.itheima.entity.Activity;
import com.itheima.request.ActivityRequest;

import java.util.List;

public interface ActivityService extends IService<Activity> {
    PageResult<Activity> findActivityByPage(ActivityRequest activityRequest);

    List<Activity> findActivityByType(Integer type);
}
