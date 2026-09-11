package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.entity.BusinessTrackRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商机跟进记录管理Mapper
 */
@Mapper
public interface BusinessTrackRecordMapper extends BaseMapper<BusinessTrackRecord> {
}