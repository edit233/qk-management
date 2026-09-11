package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.PageResult;
import com.itheima.entity.Activity;
import com.itheima.mapper.ActivityMapper;
import com.itheima.request.ActivityRequest;
import com.itheima.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public PageResult<Activity> findActivityByPage(ActivityRequest activityRequest) {
        Page<Activity> page = new Page<>(activityRequest.getPage(), activityRequest.getPageSize());
        LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(activityRequest.getChannel() != null, Activity::getChannel, activityRequest.getChannel())
                .eq(activityRequest.getType() != null, Activity::getType, activityRequest.getType());
        Page<Activity> activityPage = activityMapper.selectPage(page, wrapper);
        return new PageResult<>(activityPage.getTotal(), activityPage.getRecords());
    }

    @Override
    public List<Activity> findActivityByType(Integer type) {
        LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(type != null, Activity::getType, type);

        return activityMapper.selectList(wrapper);
    }


}
