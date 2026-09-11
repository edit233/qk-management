package com.itheima.request;

import lombok.Data;

/**
 * 标记伪线索的DTO
 */
@Data
public class MarkFalseClueRequest {
    /**
     * 伪线索原因, 1:空号、2:停机、3:竞品、4:无法联系、5:其他
     */
    private Integer reason;
    /**
     * 备注信息
     */
    private String remark;
}