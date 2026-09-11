package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.entity.OperateLog;
import com.itheima.request.LogRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LogMapper extends BaseMapper<OperateLog> {
    Page<OperateLog> getLogList(Page<OperateLog> page,@Param("logRequest")LogRequest logRequest);
}
