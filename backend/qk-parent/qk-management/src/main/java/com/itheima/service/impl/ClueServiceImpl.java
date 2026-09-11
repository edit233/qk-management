package com.itheima.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.PageResult;
import com.itheima.entity.Business;
import com.itheima.entity.Clue;
import com.itheima.entity.ClueTrackRecord;
import com.itheima.mapper.BusinessMapper;
import com.itheima.mapper.ClueMapper;
import com.itheima.mapper.ClueTrackRecordMapper;
import com.itheima.request.CluePoolRequest;
import com.itheima.request.ClueQueryRequest;
import com.itheima.request.MarkFalseClueRequest;
import com.itheima.service.ClueService;
import com.itheima.util.UserHoler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ClueServiceImpl extends ServiceImpl<ClueMapper, Clue> implements ClueService {

    @Override
    public PageResult<Clue> getPagelistClues(ClueQueryRequest clueQueryRequest) {
        Page<Clue> page = new Page<>(clueQueryRequest.getPage(), clueQueryRequest.getPageSize());
        page = this.baseMapper.getCluePageList(page, clueQueryRequest);
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    @Override
    public void assignClue(Integer clueId, Integer userId) {
        Clue clue = new Clue();
        clue.setUserId(userId);
        clue.setStatus(2);
        clue.setId(clueId);
        saveOrUpdate(clue);
    }

    @Override
    public Clue getClueById(Integer id) {
        return this.baseMapper.getClueById(id);
    }

    @Autowired
    private ClueTrackRecordMapper clueTrackRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)//指定哪些异常进行回滚
    public void trackClue(Clue clue) {
        // 更新线索状态为“跟进中”（假设状态值为3）
        clue.setStatus(3); // 跟进中
        clue.setUpdateTime(LocalDateTime.now()); // 更新时间
        this.updateById(clue);

        // 添加跟进记录
        ClueTrackRecord trackRecord = new ClueTrackRecord();
        trackRecord.setClueId(clue.getId());
        trackRecord.setUserId(UserHoler.getCurrentUser());
        trackRecord.setSubject(clue.getSubject());
        trackRecord.setLevel(clue.getLevel());
        trackRecord.setRecord(clue.getRecord());
        trackRecord.setNextTime(clue.getNextTime());
        trackRecord.setType(1); // 正常跟进
        trackRecord.setCreateTime(LocalDateTime.now());
        clueTrackRecordMapper.insert(trackRecord);
    }

    @Autowired
    private BusinessMapper businessMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void convertToBusiness(Integer id) {
        // 1. 修改线索信息
        Clue clue = this.getById(id);
        clue.setStatus(5); //转为商机
        clue.setUpdateTime(LocalDateTime.now());
        this.updateById(clue);

        // 2. 创建商机信息
        Business business = BeanUtil.copyProperties(clue, Business.class);
        business.setId(null); //  设置商机ID为空, 表示为新增商机, 主键自动增长
        business.setUserId(null); //  设置商机归属人为空
        business.setNextTime(null); //  设置下次联系时间为空
        business.setStatus(1);
        business.setClueId(clue.getId());
        business.setCreateTime(LocalDateTime.now());
        business.setUpdateTime(LocalDateTime.now());

        // 3. 插入商机记录
        businessMapper.insert(business);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void markFalseClue(Integer id, MarkFalseClueRequest markFalseClueDto) {
        // 1. 修改线索状态为"伪线索"
        Clue clue = new Clue();
        clue.setId(id);
        clue.setStatus(4); // 状态值 4 表示伪线索
        clue.setUpdateTime(LocalDateTime.now());
        this.updateById(clue);

        // 2. 添加跟进记录
        ClueTrackRecord trackRecord = new ClueTrackRecord();
        trackRecord.setClueId(id);
        trackRecord.setUserId(UserHoler.getCurrentUser()); // 当前登录用户ID
        trackRecord.setType(0); // 类型 0 表示伪线索
        trackRecord.setFalseReason(markFalseClueDto.getReason());
        trackRecord.setRecord(markFalseClueDto.getRemark());
        trackRecord.setCreateTime(LocalDateTime.now());
        clueTrackRecordMapper.insert(trackRecord);
    }

    @Override
    public PageResult<Clue> getPoolClues(CluePoolRequest cluePoolRequest) {
        Page<Clue> page = new Page<>(cluePoolRequest.getPage(), cluePoolRequest.getPageSize());
        page = baseMapper.getPoolClues(page, cluePoolRequest);
        return new PageResult<>(page.getTotal(), page.getRecords());
    }
}