package com.itheima.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.PageResult;
import com.itheima.entity.Business;
import com.itheima.entity.BusinessTrackRecord;
import com.itheima.request.BusinessPoolRequest;
import com.itheima.request.BusinessesRequest;

public interface BusinessesService extends IService<Business> {
    PageResult<Business> getBusinessPageList(BusinessesRequest businessesRequest);

    void assignBusiness( Integer businessId, Integer userId);

    void backBusiness(Integer id);

    void toCustomer(Integer id);


    Business getBusinessById(Integer id);

    void trackBusiness(Business business);

    PageResult<Business> getPoolBusiness(BusinessPoolRequest businessPoolRequest);
}
