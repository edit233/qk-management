
package com.itheima.controller;

import com.itheima.common.PageResult;
import com.itheima.common.Result;
import com.itheima.entity.Customer;
import com.itheima.request.CustomerRequest;
import com.itheima.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Result getCustomerPageList(CustomerRequest request) {
        log.info("分页查询客户列表,参数:{}", request);
        PageResult<Customer> pageResult = customerService.getCustomerPageList(request);
        return Result.success(pageResult);
    }

    @PostMapping
    public Result addCustomer(@RequestBody Customer customer) {
        log.info("添加客户,参数:{}", customer);
        customerService.save(customer);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getCustomerById(@PathVariable Integer id) {
        log.info("根据ID查询客户,参数:{}", id);
        return Result.success(customerService.getById(id));
    }

    @PutMapping
    public Result updateCustomer(@RequestBody Customer customer) {
        log.info("修改客户,参数:{}", customer);
        customerService.updateById(customer);
        return Result.success();
    }
}