package com.itheima.service.impl;


import com.itheima.mapper.BusinessMapper;
import com.itheima.mapper.ClueMapper;
import com.itheima.response.OverviewRsp;
import com.itheima.service.ReportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private BusinessMapper businessMapper;

    @Override
    public OverviewRsp getOverview() {
        //1. 获取线索概览数据
        OverviewRsp clueOverviewRsp = clueMapper.getClueOverviewData();

        //2. 获取商机概览数据
        OverviewRsp businessOverviewRsp = businessMapper.getBusinessOverviewData();

        //3. 合并数据返回
        BeanUtils.copyProperties(businessOverviewRsp, clueOverviewRsp, "clueTotal", "clueWaitAllot", "clueWaitFollow", "clueFollowing", "clueFalse", "clueConvertBusiness");
        return clueOverviewRsp;
    }
}