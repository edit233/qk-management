package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.entity.Clue;
import com.itheima.request.CluePoolRequest;
import com.itheima.request.ClueQueryRequest;
import com.itheima.response.OverviewRsp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ClueMapper extends BaseMapper<Clue> {
    Page<Clue> getCluePageList(Page<Clue> page,@Param("clueRequest") ClueQueryRequest clueQueryRequest );

    Clue getClueById(Integer id);

    /**
     * 查询线索池列表
     */
    Page<Clue> getPoolClues(Page<Clue> page,@Param("cluePoolRequest") CluePoolRequest cluePoolRequest);

    /**
     * 获取线索总览数据
     * @return 线索总览数据
     */
    OverviewRsp getClueOverviewData();
}