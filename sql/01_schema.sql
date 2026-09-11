-- ============================================================
--  qk-management 数据库建表脚本
--  ------------------------------------------------------------
--  7 张表的 DDL（activity / clue / clue_track_record / course /
--  dept / role / user），不含任何数据
--  ------------------------------------------------------------
--  ⚠️  顺序：此文件必须在 02_seed.sql 之前执行（文件名排序）
--  ⚠️  MySQL Docker 镜像按 /docker-entrypoint-initdb.d/ 字母序执行
--  ⚠️  每个表先 DROP IF EXISTS 再 CREATE，方便幂等重建
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ---------- activity ----------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id, 主键',
  `channel` tinyint unsigned NOT NULL COMMENT '渠道来源, 1:线上活动, 2:推广介绍',
  `name` varchar(20) NOT NULL COMMENT '活动名称',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `description` varchar(100) NOT NULL COMMENT '活动简介',
  `type` tinyint unsigned NOT NULL COMMENT '活动类型, 1:课程折扣, 2:代金券',
  `discount` double(2,1) DEFAULT NULL COMMENT '课程折扣',
  `voucher` int unsigned DEFAULT NULL COMMENT '代金券金额（元）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动表';

-- ---------- clue ----------
DROP TABLE IF EXISTS `clue`;
CREATE TABLE `clue` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '线索ID, 主键',
  `phone` char(11) NOT NULL COMMENT '手机号',
  `channel` tinyint unsigned NOT NULL COMMENT '渠道来源，1:线上活动, 2:推广介绍',
  `activity_id` int unsigned DEFAULT NULL COMMENT '活动信息，关联活动的ID',
  `name` varchar(20) DEFAULT NULL COMMENT '客户姓名',
  `gender` tinyint unsigned DEFAULT NULL COMMENT '性别，1:男, 2:女',
  `age` tinyint unsigned DEFAULT NULL COMMENT '年龄',
  `wechat` varchar(50) DEFAULT NULL COMMENT '微信号',
  `qq` varchar(20) DEFAULT NULL COMMENT 'QQ号',
  `user_id` int unsigned DEFAULT NULL COMMENT '归属人ID，关联用户ID',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '线索状态，1:待分配, 2:待跟进, 3:跟进中, 4:伪线索, 5:转为商机',
  `subject` tinyint unsigned DEFAULT NULL COMMENT '意向学科，1:AI智能应用开发(Java), 2:AI大模型开发(Python)，3:AI鸿蒙开发，4:AI大数据，5:AI嵌入式，6:AI测试，7:AI运维',
  `level` tinyint unsigned DEFAULT NULL COMMENT '意向等级, 1:近期学习、2:打算学习(考虑中)、3:进行了解、4:打酱油',
  `next_time` datetime DEFAULT NULL COMMENT '下次跟进时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='线索表';

-- ---------- clue_track_record ----------
DROP TABLE IF EXISTS `clue_track_record`;
CREATE TABLE `clue_track_record` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '跟进记录ID, 主键',
  `clue_id` int unsigned NOT NULL COMMENT '线索ID，关联线索ID',
  `user_id` int unsigned NOT NULL COMMENT '跟进人ID，关联用户ID',
  `subject` tinyint unsigned DEFAULT NULL COMMENT '意向学科，1:AI智能应用开发(Java), 2:AI大模型开发(Python)，3:AI鸿蒙开发，4:AI大数据，5:AI嵌入式，6:AI测试，7:AI运维',
  `level` tinyint unsigned DEFAULT NULL COMMENT '意向等级, 1:近期学习、2:打算学习(考虑中)、3:进行了解、4:打酱油',
  `record` varchar(100) DEFAULT NULL COMMENT '跟进记录',
  `next_time` datetime DEFAULT NULL COMMENT '下次跟进时间',
  `type` tinyint unsigned DEFAULT NULL COMMENT '跟进类型, 1:正常跟进、0:伪线索',
  `false_reason` tinyint unsigned DEFAULT NULL COMMENT '伪线索原因, 1:空号、2:停机、3:竞品、4:无法联系、5:其他',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='线索跟进记录表';

-- ---------- course ----------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '课程id, 主键',
  `subject` tinyint unsigned NOT NULL COMMENT '课程学科，1:AI智能应用开发(Java), 2:AI大模型开发(Python)，3:AI鸿蒙开发，4:AI大数据，5:AI嵌入式，6:AI测试，7:AI运维',
  `name` varchar(20) NOT NULL COMMENT '课程名称',
  `price` int unsigned NOT NULL COMMENT '课程价格（元）',
  `target` tinyint unsigned NOT NULL COMMENT '适用人群, 1:小白学员, 2:初级程序员, 3:中级程序员',
  `description` varchar(100) DEFAULT NULL COMMENT '课程介绍',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='课程表';

-- ---------- dept ----------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '部门id，主键',
  `name` varchar(10) NOT NULL COMMENT '部门名称',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态：0-停用，1-正常',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门信息表';

-- ---------- role ----------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) NOT NULL COMMENT '角色名称',
  `label` varchar(64) NOT NULL COMMENT '角色标识',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_label` (`label`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

-- ---------- user ----------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id, 主键',
  `username` varchar(20) NOT NULL COMMENT '用户名，唯一',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `phone` char(11) NOT NULL COMMENT '手机号，唯一',
  `email` varchar(50) NOT NULL COMMENT '邮箱，唯一',
  `gender` tinyint unsigned NOT NULL COMMENT '性别，1: 男，2: 女',
  `status` tinyint unsigned NOT NULL COMMENT '状态，1: 正常，0: 停用',
  `dept_id` int unsigned DEFAULT NULL COMMENT '部门id，关联部门表主键',
  `role_id` int unsigned DEFAULT NULL COMMENT '角色id，关联角色表主键',
  `image` varchar(255) DEFAULT NULL COMMENT '头像url',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注，50字以内',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

SET FOREIGN_KEY_CHECKS = 1;
