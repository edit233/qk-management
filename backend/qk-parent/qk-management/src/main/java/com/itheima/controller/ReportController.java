package com.itheima.controller;

import com.itheima.common.Result;
import com.itheima.response.OverviewRsp;
import com.itheima.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 获取首页概览数据 - /report/overview
     */
    @GetMapping("/overview")
    public Result getOverview() {
        log.info("获取首页概览数据");
        OverviewRsp overview = reportService.getOverview();
        return Result.success(overview);
    }

}