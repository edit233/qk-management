
package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.PageResult;
import com.itheima.entity.Customer;
import com.itheima.request.CustomerRequest;

public interface CustomerService extends IService<Customer> {
    PageResult<Customer> getCustomerPageList(CustomerRequest request);
}