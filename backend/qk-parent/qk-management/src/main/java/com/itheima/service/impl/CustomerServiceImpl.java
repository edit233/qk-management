
package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.PageResult;
import com.itheima.entity.Customer;
import com.itheima.mapper.CustomerMapper;
import com.itheima.request.CustomerRequest;
import com.itheima.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public PageResult<Customer> getCustomerPageList(CustomerRequest request) {
        Page<Customer> page = new Page<>(request.getPage(), request.getPageSize());
        Page<Customer> result = customerMapper.getCustomerPageList(page, request);
        return new PageResult<>(result.getTotal(), result.getRecords());
    }
}