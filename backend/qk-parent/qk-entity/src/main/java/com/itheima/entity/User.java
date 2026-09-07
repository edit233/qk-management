package com.itheima.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private Integer gender;
    private Integer status;
    private Long deptId;
    private Long roleId;
    private String image;
    private String remark;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    //扩展属性
    @TableField(exist = false)//表示这个字段在数据库中不存在
    private String deptName; //部门名称
    @TableField(exist = false)//表示这个字段在数据库中不存在
    private String roleName; //角色名称
}
