package com.itheima.controller;


import com.itheima.common.Result;
import com.itheima.request.LogRequest;
import com.itheima.service.impl.LogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/logs")
public class LogController {
    @Autowired
    private LogServiceImpl logService;

    @GetMapping
    public Result getLogList(LogRequest logRequest){
        log.info("日志列表查询,参数:{}",logRequest);
        return Result.success(logService.logPageResult(logRequest));
    }
}
