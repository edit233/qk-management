package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.entity.Business;
import com.itheima.request.BusinessPoolRequest;
import com.itheima.request.BusinessesRequest;
import com.itheima.response.OverviewRsp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商机管理Mapper
 */
@Mapper
public interface BusinessMapper extends BaseMapper<Business> {
    Page<Business> getBusinessPageList(Page<Business> page, @Param("BusinessesRequest") BusinessesRequest businessesRequest);

    Business getBusinessById(Integer id);

    Page<Business> getPoolClues(Page<Business> page,@Param("BusinessPoolRequest") BusinessPoolRequest businessPoolRequest);

    /**
     * 获取商机概览数据
     */
    OverviewRsp getBusinessOverviewData();
}