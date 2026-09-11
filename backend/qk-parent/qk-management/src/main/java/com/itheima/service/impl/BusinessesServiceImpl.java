package com.itheima.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.PageResult;
import com.itheima.entity.Business;
import com.itheima.entity.BusinessTrackRecord;
import com.itheima.entity.Clue;
import com.itheima.entity.Customer;
import com.itheima.mapper.BusinessMapper;
import com.itheima.mapper.BusinessTrackRecordMapper;
import com.itheima.mapper.CustomerMapper;
import com.itheima.request.BusinessPoolRequest;
import com.itheima.request.BusinessesRequest;
import com.itheima.service.BusinessesService;
import com.itheima.util.UserHoler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BusinessesServiceImpl extends ServiceImpl<BusinessMapper, Business> implements BusinessesService {
    @Autowired
    private BusinessTrackRecordMapper businessTrackRecordMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public PageResult<Business> getBusinessPageList(BusinessesRequest businessesRequest) {
        Page<Business> page = new Page<>(businessesRequest.getPage(), businessesRequest.getPageSize());
        page = this.baseMapper.getBusinessPageList(page, businessesRequest);
        return new PageResult<>(page.getTotal(),page.getRecords());
    }

    @Override
    public void assignBusiness(Integer businessId, Integer userId) {
        Business business = new Business();
        business.setId(businessId);
        business.setUserId(userId);
        business.setStatus(2);
        saveOrUpdate(business);
    }

    @Override
    public void backBusiness(Integer id) {
        Business business = new Business();
        business.setId(id);
        business.setStatus(4);
        business.setCreateTime(LocalDateTime.now());
        updateById(business);

        BusinessTrackRecord businessTrackRecord = new BusinessTrackRecord();
        businessTrackRecord.setBusinessId(id);
        businessTrackRecord.setUserId(UserHoler.getCurrentUser());
        businessTrackRecord.setTrackStatus(2);
        businessTrackRecord.setCreateTime(LocalDateTime.now());
        businessTrackRecordMapper.insert(businessTrackRecord);
    }

    @Override
    @Transactional
    public void toCustomer(Integer id){
        Business business = this.getById(id);

        Business updateBusiness = new Business();
        updateBusiness.setId(id);
        updateBusiness.setStatus(5);
        updateBusiness.setUpdateTime(LocalDateTime.now());
        this.updateById(updateBusiness);

        Customer customer = BeanUtil.copyProperties(business, Customer.class);
        customer.setId(null);
        customer.setBusinessId(id);
        customer.setCreateTime(LocalDateTime.now());
        customer.setUpdateTime(LocalDateTime.now());
        customerMapper.insert(customer);
    }

    @Override
    public Business getBusinessById(Integer id) {
        return this.baseMapper.getBusinessById(id);
    }
    @Transactional
    @Override
    public void trackBusiness(Business business) {
        // 更新线索状态为“跟进中”（假设状态值为3）
        business.setStatus(3); // 跟进中
        business.setUpdateTime(LocalDateTime.now()); // 更新时间
        this.updateById(business);

        // 添加跟进记录
        BusinessTrackRecord trackRecord = new BusinessTrackRecord();
        trackRecord.setBusinessId(business.getId());
        trackRecord.setUserId(UserHoler.getCurrentUser());
        trackRecord.setTrackStatus(business.getStatus());
        trackRecord.setKeyItems(CollUtil.join(business.getKeyItems(), ","));
        trackRecord.setRecord(business.getRecord());
        trackRecord.setNextTime(business.getNextTime());
        trackRecord.setCreateTime(LocalDateTime.now());
        businessTrackRecordMapper.insert(trackRecord);
    }

    @Override
    public PageResult<Business> getPoolBusiness(BusinessPoolRequest businessPoolRequest) {
        Page<Business> page = new Page<>(businessPoolRequest.getPage(), businessPoolRequest.getPageSize());
        page = baseMapper.getPoolClues(page, businessPoolRequest);
        return new PageResult<>(page.getTotal(), page.getRecords());
    }
}
