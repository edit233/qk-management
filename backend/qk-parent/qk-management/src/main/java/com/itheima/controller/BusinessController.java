package com.itheima.controller;


import com.itheima.common.PageResult;
import com.itheima.common.Result;
import com.itheima.entity.Business;
import com.itheima.entity.Clue;
import com.itheima.request.BusinessPoolRequest;
import com.itheima.request.BusinessesRequest;
import com.itheima.service.impl.BusinessesServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/businesses")
public class BusinessController {
    @Autowired
    private BusinessesServiceImpl businessesService;

    @GetMapping
    public Result getBusinessesPageList(BusinessesRequest businessesRequest) {
        log.info("分页查询商机,查询参数:{}", businessesRequest);
        return Result.success(businessesService.getBusinessPageList(businessesRequest));
    }

    @PostMapping
    public Result addBusiness(@RequestBody Business business) {
        business.setStatus(1);
        businessesService.saveOrUpdate(business);
        log.info("添加商机,参数:{}", business);
        return Result.success();
    }


    @PutMapping("/assign/{businessId}/{userId}")
    public Result assignBusiness(@PathVariable Integer businessId, @PathVariable Integer userId) {
        log.info("分配商机,参数:businessId={},userId={}", businessId, userId);
        businessesService.assignBusiness(businessId, userId);
        return Result.success();
    }

    @PutMapping("/back/{id}")
    public Result backBusiness(@PathVariable Integer id) {
        log.info("踢回公海,参数:{}", id);
        businessesService.backBusiness(id);
        return Result.success();
    }

    @PostMapping("/toCustomer/{id}")
    public Result toCustomer(@PathVariable Integer id){
        log.info("转客户,参数:{}",id);
        businessesService.toCustomer(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getBusinessById(@PathVariable Integer id){
        log.info("根据id查询商机,参数:{}",id);
        return Result.success(businessesService.getBusinessById(id));
    }

    @PutMapping
    public Result trackBusiness(@RequestBody Business business){
        log.info("跟进商机,参数:{}",business);
        businessesService.trackBusiness(business);
        return Result.success();
    }

    @GetMapping("/pool")
    public Result getPoolBusiness(BusinessPoolRequest businessPoolRequest){
        log.info("公海池分页查询,参数:{}",businessPoolRequest);
        PageResult<Business> pageResult = businessesService.getPoolBusiness(businessPoolRequest);
        return Result.success(pageResult);
    }
}
