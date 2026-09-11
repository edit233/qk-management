package com.itheima.controller;

import com.itheima.common.PageResult;
import com.itheima.common.Result;
import com.itheima.entity.Clue;
import com.itheima.request.CluePoolRequest;
import com.itheima.request.ClueQueryRequest;
import com.itheima.request.MarkFalseClueRequest;
import com.itheima.service.impl.ClueServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/clues")
public class ClueController {

    @Autowired
    private ClueServiceImpl clueService;

    /**
     * 新增线索
     *
     * @param clue 线索
     */
    @PostMapping
    public Result addClue(@RequestBody Clue clue) {
        log.info("新增线索: {}", clue);
        clue.setStatus(1);//1:待分配
        clueService.saveOrUpdate(clue);
        return Result.success();
    }

    /**
     * 列表查询
     *
     * @param clueQueryRequest 查询参数
     * @return 查询结果
     */
    @GetMapping
    public Result listClues(ClueQueryRequest clueQueryRequest) {
        log.info("查询参数: {}", clueQueryRequest);
        PageResult<Clue> pageResult = clueService.getPagelistClues(clueQueryRequest);
        return Result.success(pageResult);
    }

    @PutMapping("/assign/{clueId}/{userId}")
    public Result assignClue(@PathVariable Integer clueId, @PathVariable Integer userId) {
        log.info("分配线索: 线索ID={}, 用户ID={}", clueId, userId);

        clueService.assignClue(clueId,userId );
        return Result.success();
    }

    /**
     * 根据ID查询线索详细信息
     * @param id 线索ID
     * @return 线索详细信息
     */
    @GetMapping("/{id}")
    public Result getClueById(@PathVariable Integer id) {
        log.info("根据ID查询线索详细信息, id: {}", id);
        Clue clue = clueService.getClueById(id);
        return Result.success(clue);
    }

    /**
     * 跟进线索
     *
     * @param clue 线索信息
     * @return Result
     */
    @PutMapping
    public Result trackClue(@RequestBody Clue clue) {
        log.info("跟进线索: {}", clue);
        clueService.trackClue(clue);
        return Result.success();
    }

    /**
     * 将线索转为商机
     *
     * @param id 线索ID
     * @return 操作结果
     */
    @PutMapping("/toBusiness/{id}")
    public Result convertToBusiness(@PathVariable Integer id) {
        log.info("将线索转为商机, id: {}", id);
        clueService.convertToBusiness(id);
        return Result.success();
    }

    /**
     * 将线索标记为伪线索
     *
     * @param id 线索id
     * @param markFalseClueRequest 伪线索
     */
    @PutMapping("/false/{id}")
    public Result markFalseClue(@PathVariable Integer id, @RequestBody MarkFalseClueRequest markFalseClueRequest) {
        log.info("将线索标记为伪线索, id: {}, {}", id, markFalseClueRequest);
        clueService.markFalseClue(id, markFalseClueRequest);
        return Result.success();
    }

    /**
     * 查询线索池列表
     *
     * @param cluePoolRequest 查询参数
     */
    @GetMapping("/pool")
    public Result getPoolClues(CluePoolRequest cluePoolRequest) {
        log.info("查询线索池列表,  {}", cluePoolRequest);
        PageResult<Clue> pageResult = clueService.getPoolClues(cluePoolRequest);
        return Result.success(pageResult);
    }
}