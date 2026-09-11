package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.common.PageResult;
import com.itheima.entity.Clue;
import com.itheima.request.CluePoolRequest;
import com.itheima.request.ClueQueryRequest;
import com.itheima.request.MarkFalseClueRequest;

public interface ClueService extends IService<Clue> {

    PageResult<Clue> getPagelistClues(ClueQueryRequest clueQueryRequest);

    void assignClue(Integer clueId, Integer userId);

    Clue getClueById(Integer id);


    /**
     * 跟进线索
     *
     * @param clue 线索信息
     */
    void trackClue(Clue clue);

    /**
     * 将线索转为商机
     * @param id 线索ID
     */
    void convertToBusiness(Integer id);

    /**
     * 伪线索
     *
     * @param id 线索ID
     * @param markFalseClueRequest 伪线索信息
     */
    void markFalseClue(Integer id, MarkFalseClueRequest markFalseClueRequest);

    /**
     * 查询线索池列表
     *
     * @param cluePoolRequest 查询参数
     * @return 线索列表
     */
    PageResult<Clue> getPoolClues(CluePoolRequest cluePoolRequest);

}