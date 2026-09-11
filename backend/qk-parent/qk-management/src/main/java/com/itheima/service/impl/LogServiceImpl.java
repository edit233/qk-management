package com.itheima.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.PageResult;
import com.itheima.entity.OperateLog;
import com.itheima.mapper.LogMapper;
import com.itheima.request.LogRequest;
import com.itheima.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, OperateLog>implements LogService {
    @Autowired
    private LogMapper logMapper;
    public PageResult<OperateLog> logPageResult(LogRequest logRequest){
        Page<OperateLog> page = new Page<>(logRequest.getPage(),logRequest.getPageSize());
        page = logMapper.getLogList(page,logRequest);
        return new PageResult<>(page.getTotal(),page.getRecords());
    }
}
