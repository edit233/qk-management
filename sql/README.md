# 数据库初始化文件

本目录包含 MySQL 8.0 数据库的初始化脚本，部署时由 MySQL Docker 镜像自动执行。

---

## 文件清单

| 文件 | 顺序 | 作用 |
|------|------|------|
| `01_schema.sql` | 第 1 个 | 全部 7 张表的 DDL（建表语句），不含数据 |
| `02_seed.sql`   | 第 2 个 | 演示用测试数据（已脱敏，4 个测试账号） |

文件名以数字前缀确保按预期顺序执行。MySQL Docker 镜像在 `/docker-entrypoint-initdb.d/` 下按字母序执行所有 `.sql` / `.sql.gz` / `.sh` 文件。

> ⚠️ **首次启动时才会执行**（数据目录为空）。后续重启 / 升级镜像不会重复执行。

---

## 表结构概览

| 表 | 用途 | 主要字段 |
|----|------|---------|
| `dept`              | 部门 | id, name, status |
| `role`              | 角色 | id, name, label（权限标识）, remark |
| `user`              | 用户 | id, username, password, name, dept_id, role_id |
| `course`            | 课程 | id, subject, name, price, target |
| `activity`          | 营销活动 | id, channel, type, discount/voucher |
| `clue`              | 销售线索 | id, phone, name, status, user_id, subject, level |
| `clue_track_record` | 线索跟进记录 | id, clue_id, user_id, record, next_time |

完整 DDL 见 [`01_schema.sql`](01_schema.sql)。

---

## 默认账号

种子数据（`02_seed.sql`）包含 4 个测试账号，密码均为 `123456`：

| 用户名 | 角色 | 用途 |
|--------|------|------|
| `admin`         | 系统管理员 | 全权限 |
| `clue_user`     | 线索专员   | 跟进线索 |
| `business_user` | 商机专员   | 处理商机 |
| `viewer`        | 普通员工   | 只读 |

> ⚠️ **生产环境请第一时间修改密码或删除种子数据**！

### 密码哈希说明

`02_seed.sql` 中填的是 `MD5('123456')`：

```
e10adc3949ba59abbe56e057f20f883e
```

如果项目后端用的是 **BCrypt**（推荐），需要替换为 BCrypt 哈希。可以用以下 Java 代码生成：

```java
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String hash = encoder.encode("123456");
System.out.println(hash);
```

然后把 `02_seed.sql` 中的所有 `password` 字段替换为该哈希值。

---

## MySQL Docker 自动初始化机制

`docker-compose.yml` 把整个 `./sql` 目录挂载到容器内的 `/docker-entrypoint-initdb.d/`：

```yaml
mysql:
  image: mysql:8.0
  volumes:
    - mysql_data:/var/lib/mysql
    - ./sql:/docker-entrypoint-initdb.d
```

**触发条件**：`mysql_data` 卷为空（首次启动）时执行；后续启动不会重复。

**重置数据库**（⚠️ 删数据）：

```bash
docker compose down mysql
docker volume rm qk-management_mysql_data
docker compose up -d mysql
```

---

## 本地手动初始化

如果不用 Docker，可以手动执行：

```bash
mysql -u root -p
> CREATE DATABASE qk DEFAULT CHARACTER SET utf8mb4;
> exit

mysql -u root -p qk < sql/01_schema.sql
mysql -u root -p qk < sql/02_seed.sql
```

---

## 添加新的测试数据

按以下顺序操作：

1. **不要直接修改 `02_seed.sql`**（已有数据，避免破坏历史快照）
2. 创建 `03_xxx.sql`（按字母序在 seed 之后执行）
3. 文件里只放 `INSERT INTO ... VALUES (...)` 语句，**不要**再 `CREATE TABLE`
4. 重新初始化：删除 `mysql_data` 卷后 `docker compose up -d mysql`

---

## 添加新的迁移（生产）

如果生产数据库已经上线，需要新增字段/索引，使用 Flyway / Liquibase 等迁移工具，**不要**直接改 `01_schema.sql`：

```sql
-- sql/migration/V2__add_user_avatar.sql
ALTER TABLE user ADD COLUMN avatar VARCHAR(255) DEFAULT NULL;
```

并按日期版本号命名，避免与初始化脚本冲突。