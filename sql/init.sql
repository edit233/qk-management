-- 创建 qk 用户并授权（Docker 初始化时自动执行）
CREATE USER IF NOT EXISTS 'qk'@'%' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON qk.* TO 'qk'@'%';
FLUSH PRIVILEGES;
-- MySQL dump 10.13  Distrib 8.4.9, for Win64 (x86_64)
--
-- Host: localhost    Database: qk
-- ------------------------------------------------------
-- Server version	8.4.9

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES (1,1,'618-AI训练营折扣','2025-05-31 16:00:00','2025-06-30 15:59:59','618-AI训练营折扣',2,8.5,1,'2025-05-14 17:49:58','2026-09-07 20:22:00'),(2,2,'B站推广介绍','2025-05-19 16:00:00','2025-06-05 15:59:59','B站推广介绍-全新课程升级',2,NULL,300,'2025-05-15 10:51:23','2025-05-15 10:51:23'),(3,1,'测试活动1','2025-06-01 00:00:00','2025-06-30 23:59:59','描述1',1,7.5,200,'2025-05-15 11:09:04','2025-05-15 11:09:04'),(4,2,'测试活动2','2025-07-01 00:00:00','2025-07-31 23:59:59','描述2',2,NULL,250,'2025-05-15 11:09:04','2025-05-15 11:09:04'),(5,1,'测试活动3','2025-08-01 00:00:00','2025-08-31 23:59:59','描述3',1,9.0,NULL,'2025-05-15 11:09:04','2025-05-15 11:09:04'),(6,2,'测试活动4','2025-09-01 00:00:00','2025-09-30 23:59:59','描述4',2,6.5,150,'2025-05-15 11:09:04','2025-05-15 11:09:04'),(7,1,'测试活动5','2025-10-01 00:00:00','2025-10-31 23:59:59','描述5',1,8.0,300,'2025-05-15 11:09:04','2025-05-15 11:09:04'),(8,2,'测试活动6','2025-11-01 00:00:00','2025-11-30 23:59:59','描述6',2,NULL,100,'2025-05-15 11:09:04','2025-05-15 11:09:04'),(9,1,'测试活动7','2025-12-01 00:00:00','2025-12-31 23:59:59','描述7',1,7.0,NULL,'2025-05-15 11:09:04','2025-05-15 11:09:04'),(11,1,'测试活动9','2026-02-01 00:00:00','2026-02-28 23:59:59','描述9',1,9.5,250,'2025-05-15 11:09:04','2025-05-15 11:09:04'),(12,1,'暑期超值优惠活动','2025-06-24 16:00:00','2025-07-25 15:59:59','暑期超值优惠活动, 主要是针对于暑期大学生',2,NULL,800,'2025-06-22 14:47:34','2025-06-22 14:47:34');
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clue`
--

DROP TABLE IF EXISTS `clue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clue`
--

LOCK TABLES `clue` WRITE;
/*!40000 ALTER TABLE `clue` DISABLE KEYS */;
INSERT INTO `clue` VALUES (1,'14700000001',2,3,'张思',1,23,'wx2324342323','3434343423',2,3,NULL,NULL,'2025-06-25 10:00:00','2025-05-24 17:13:50','2025-06-21 15:11:34'),(2,'13589890912',1,2,'李久阶',1,22,'wx28392839','2323232323',NULL,1,1,NULL,NULL,'2025-05-24 17:16:03','2025-06-21 15:08:16'),(3,'13909120913',2,3,'李迪',1,22,'wx17327323','2456754323',NULL,1,1,NULL,NULL,'2025-05-28 11:54:02','2025-07-20 18:08:18'),(4,'13688889991',1,2,'钱四',1,21,'wxdjjd92922','2345643236',NULL,1,1,NULL,NULL,'2025-05-28 14:16:55','2025-06-10 21:15:03'),(5,'15509091231',1,4,'欧斯卡',2,22,'wx283232423','23456789657',NULL,1,NULL,NULL,NULL,'2025-05-28 19:41:46','2025-05-29 09:48:05'),(6,'17709092901',1,10,'奚梦',1,22,'jsdhfsf2324','3456789432',NULL,1,1,NULL,NULL,'2025-05-28 19:44:15','2025-06-10 21:04:18'),(7,'13589898881',2,11,'伊斯科',1,19,'wx8943895345','34343232536',NULL,1,2,NULL,NULL,'2025-05-28 19:45:06','2025-06-09 14:42:38'),(8,'13511110000',2,13,'齐欧式',1,30,'wxqi299232','2435676543',NULL,1,2,NULL,NULL,'2025-05-29 10:45:07','2025-06-22 14:52:08'),(9,'13511110001',1,2,'李秋菊',2,22,'liqiuju23874','234567865',NULL,1,1,NULL,NULL,'2025-05-29 10:47:22','2025-06-22 14:51:37'),(10,'13398980102',1,2,'张岱',1,22,'wx13728785434','234567543',NULL,1,1,NULL,NULL,'2025-06-16 15:31:22','2025-07-20 20:59:46'),(11,'15508761231',1,1,'卫丹',2,24,'wxweidan1212','8450313640',NULL,1,NULL,NULL,NULL,'2025-07-16 18:46:10','2025-07-20 15:47:32'),(12,'17792098192',2,3,'张吉',1,21,'wx289483544','245676856',NULL,1,NULL,NULL,NULL,'2025-07-20 15:17:56','2025-07-20 15:43:56');
/*!40000 ALTER TABLE `clue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clue_track_record`
--

DROP TABLE IF EXISTS `clue_track_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clue_track_record`
--

LOCK TABLES `clue_track_record` WRITE;
/*!40000 ALTER TABLE `clue_track_record` DISABLE KEYS */;
INSERT INTO `clue_track_record` VALUES (1,1,2,1,1,'无','2025-05-29 10:00:00',1,NULL,'2025-05-29 10:25:00'),(2,1,2,1,1,'有意向,目前大三','2025-05-30 10:00:00',1,NULL,'2025-05-29 10:25:00');
/*!40000 ALTER TABLE `clue_track_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (2,2,'Python核心与AI开发基础',40,1,'主要讲解Python核心与AI开发基础','2025-05-09 21:30:00','2026-09-04 21:11:26'),(3,1,'AI驱动Web开发',2800,2,'AI驱动Web开发， 主要讲解Web开发的核心知识及Web项目的设计、开发、测试、部署','2025-05-22 11:01:40','2025-05-22 11:01:55'),(4,1,'企业级物联网项目',4500,2,'企业级物联网项目，主要讲解物联网项目的设计、开发、测试、部署全流程交付','2025-05-22 11:02:44','2025-05-22 11:02:44'),(6,1,'SpringAI大模型应用开发',1990,3,'SpringAI大模型应用开发','2025-05-22 11:08:26','2025-05-22 11:08:26'),(7,2,'数据分析',2000,2,'基于Python数据分析的','2025-05-22 21:44:49','2025-05-22 21:45:50'),(8,3,'前端基础',28,1,'前端基础，主要讲解HTML、CSS、JS等前端开发基础知识','2025-05-23 20:07:47','2025-05-23 20:07:47'),(9,2,'LangChain入门',1800,2,'LangChain入门，学习该框架如何操作AI大模型','2025-05-26 21:43:57','2025-05-26 21:44:26'),(10,1,'LangChain4j',299,2,'LangChain4j从入门到进阶, 适合AI初学者','2025-06-19 19:03:17','2025-06-19 19:03:17'),(11,1,'RAG增强检索',399,2,'RAG','2025-06-19 19:04:24','2025-06-19 19:04:24'),(12,1,'SpringCloud微服务框架',1200,2,'SpringCloud微服务框架及分布式解决方案','2025-06-22 14:41:16','2025-06-22 14:41:16'),(13,1,'微服务智能项目集',3899,2,'微服务智能项目集','2025-06-22 14:44:06','2025-06-22 14:44:06'),(14,1,'AI智能体项目-天机AI助理',4999,2,'AI智能体项目-天机AI助理','2025-06-22 14:44:31','2025-06-22 14:44:31');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dept`
--

DROP TABLE IF EXISTS `dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dept` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '部门id，主键',
  `name` varchar(10) NOT NULL COMMENT '部门名称',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态：0-停用，1-正常',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dept`
--

LOCK TABLES `dept` WRITE;
/*!40000 ALTER TABLE `dept` DISABLE KEYS */;
INSERT INTO `dept` VALUES (2,'销售部',1,'2025-04-26 15:45:31','2026-09-04 19:34:36'),(3,'销售一部',1,'2025-04-26 15:45:31','2025-07-26 15:27:46'),(4,'人力资源部',1,'2025-04-26 15:45:31','2025-07-26 15:27:46'),(5,'财务部',1,'2025-04-26 15:45:31','2025-07-26 15:28:04'),(6,'客服部',0,'2025-04-26 15:45:31','2025-07-26 15:28:04'),(7,'技术支持部',1,'2025-04-26 15:45:31','2025-07-26 15:28:08'),(8,'产品部',1,'2025-04-26 15:45:31','2025-07-26 15:28:08'),(9,'运营部',0,'2025-04-26 15:45:31','2025-08-06 21:36:43'),(11,'法务部',0,'2025-04-26 15:45:31','2025-08-06 21:36:40'),(12,'设计部',1,'2025-04-26 15:45:31','2025-07-26 15:28:18'),(13,'公关部',1,'2025-04-26 15:45:31','2025-08-06 11:50:27'),(15,'战略部',1,'2025-04-26 15:45:31','2025-07-27 18:16:40'),(16,'市场一部',1,'2025-07-09 11:32:42','2025-07-26 15:28:25'),(17,'服务中心',1,'2025-07-27 09:56:01','2025-07-27 09:56:01'),(20,'教研一部',1,'2025-07-27 10:39:43','2025-07-27 10:39:43'),(21,'教研二部',1,'2025-07-27 10:40:24','2025-07-27 10:40:24'),(44,'战略投资部',1,'2025-08-06 17:49:27','2025-08-06 21:35:18'),(47,'公关部66',1,'2025-08-07 18:20:12','2025-08-07 19:43:28');
/*!40000 ALTER TABLE `dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'管理员','admin','管理员, 用于管理整个系统数据','2025-05-09 19:47:28','2025-05-09 19:47:28'),(2,'线索专员','clue_operator','线索专员','2025-05-09 19:48:00','2025-05-09 19:48:00'),(3,'商机专员','business_operator','商机专员','2025-05-09 20:03:04','2025-05-09 20:03:04'),(4,'普通用户','common_user','普通公司员工','2025-06-21 15:25:30','2025-08-06 21:36:23'),(9,'测试角色','role_test','测试角色','2025-08-06 21:35:30','2025-08-06 21:35:41');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'zhangsan','4e7bdb88640b376ac6646b8f1ecfb558','张三','13800138001','admin@example.com',1,1,6,1,'https://web2025-test.oss-cn-beijing.aliyuncs.com/ee089310-479b-4672-a74a-dc680fa6a18a.png','系统管理员','2025-05-12 11:39:03','2025-06-21 15:25:55'),(2,'lisi','c3cb6d12c40908943b64bc0681af47db','李四','13800138002','editor1@example.com',1,1,4,2,'https://web2025-test.oss-cn-beijing.aliyuncs.com/62fcb582-074c-489a-8ea8-721bc8bba042.png','内容编辑','2025-05-12 11:39:03','2025-06-21 15:26:00'),(3,'sunwuji','368a7fccd730d807f41e2161798e13ca','孙无忌','18809091111','sunwuji@163.com',1,1,5,2,'https://web2025-test.oss-cn-beijing.aliyuncs.com/e65781f3-c87d-46e2-9d53-cf4329ce64e6.png','孙无忌刚入职，先在销售部磨炼磨炼','2025-05-12 16:46:21','2025-07-14 18:13:45'),(4,'songjiang','a91b1cfe31e6b534537ab992e06380f8','宋江','13800000001','songjiang@example.com',1,1,5,2,'https://web2025-test.oss-cn-beijing.aliyuncs.com/0bdd90db-17e1-407c-b922-27d987c9cc45.png','Remark 1','2025-05-14 15:49:31','2025-07-14 18:13:45'),(5,'lujunyi','ef73dd11149e096116f755a171eb3fec','卢俊义','13800000002','lujunyi@example.com',1,1,3,7,'https://web2025-test.oss-cn-beijing.aliyuncs.com/1564f4d9-7d07-4ea4-9e23-ecebe39d3520.png','Remark 2','2025-05-14 15:49:31','2025-07-14 18:13:45'),(6,'wuyong','d74bcf7b7f805922e4ad42315f3a8cde','吴用','13800000003','wuyong@example.com',1,0,7,2,'https://web2025-test.oss-cn-beijing.aliyuncs.com/cb074a33-4f30-4b3e-b9f1-bf8bf9d0f4ff.png','Remark 3','2025-05-14 15:49:31','2025-07-14 18:13:45'),(7,'gongsunsheng','ee3a229f2f3d2bd7c9dd3eeb4eda02b3','公孙胜','13800000004','gongsunsheng@example.com',1,1,5,2,'https://web2025-test.oss-cn-beijing.aliyuncs.com/284efce8-0b0c-4076-bbf6-3e104446ce70.png','Remark 4','2025-05-14 15:49:31','2025-07-14 18:13:45'),(8,'linchong','786271307576f1ee762341173a2caa48','林冲','13800000006','linchong@example.com',1,1,4,3,'https://web2025-test.oss-cn-beijing.aliyuncs.com/d2fbc991-c6de-4871-bd91-4a7d186b1f90.png','Remark 6','2025-05-14 15:49:31','2025-07-14 18:13:45'),(9,'qinming','23b9c4ea1f4d1844acd20a5314da8553','秦明','13800000007','qinming@example.com',1,1,5,2,'https://web2025-test.oss-cn-beijing.aliyuncs.com/d87de2cc-79a4-4a56-af9c-eb6aca52db66.png','Remark 7','2025-05-14 15:49:31','2025-07-14 18:13:45'),(10,'huarong','19d59a4e1e63552228b73b2cb2b5fcdd','花荣','13800000008','huayong@example.com',1,1,6,1,'https://web2025-test.oss-cn-beijing.aliyuncs.com/4775c1a5-4944-4aec-9753-9fa622dbbe75.png','Remark 8','2025-05-14 15:49:31','2025-07-14 18:13:45'),(11,'huyan','fbb39f3019cc4f5444dbd6b623f71a1a','呼延灼','13800000009','huyan@example.com',1,0,8,2,'https://web2025-test.oss-cn-beijing.aliyuncs.com/446aa6db-85b0-451f-bc11-92a52a518192.png','Remark 9','2025-05-14 15:49:31','2025-07-14 18:13:45'),(12,'huaqian','1007f13423a57bc0390d9510f96d0322','花千朵','13800000010','huaqian@example.com',2,1,10,3,'https://web2025-test.oss-cn-beijing.aliyuncs.com/63137d09-0734-4910-9502-b9b8d063b132.png','Remark 10','2025-05-14 15:49:31','2025-07-14 18:13:45'),(13,'shaqianmo','29d6fb85ac49d6d82b9b6dfd5f62939a','杀阡陌','13509091456','shaqianmo@163.com',0,1,5,2,'https://web2025-test.oss-cn-beijing.aliyuncs.com/ff8646a3-629b-4124-a2cb-f0365f9075c2.png','','2025-06-19 17:48:39','2025-07-14 18:13:45'),(14,'baizihua','f1659bd8a56022d817d6bd124c6181e8','白子画','13609091206','baizihua@163.com',1,1,3,7,'https://web2025-test.oss-cn-beijing.aliyuncs.com/421c0945-6f25-405c-b439-9b8b997338aa.png','系统研发','2025-06-22 14:33:49','2025-07-14 18:13:45'),(15,'huaqiangu','be6485aab12634baf922b3dbe407fcda','花千骨','15809092819','huaqiangu@163.com',0,1,6,7,'https://web2025-test.oss-cn-beijing.aliyuncs.com/735320dc-ac08-4b73-bdd7-7f0cf054274f.png','花千骨是一个普通用户','2025-06-24 10:40:20','2025-07-14 18:13:45'),(16,'kuangyetian','5e3c47c0d266439d7ce704972c92086a','旷野天','13689281921','kuangyetian@163.com',1,1,9,7,'https://web2025-test.oss-cn-beijing.aliyuncs.com/5d1f8e0f-2227-4469-8c90-7a02867fbe70.png','新入职的技术员旷野天','2025-06-27 19:38:34','2025-07-14 18:13:45');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-08  9:44:38

