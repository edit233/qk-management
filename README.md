# qk-management

> 客户管理 / CRM 系统 · Spring Boot 3.5 + Nginx + MySQL 8.0 + Docker · **推代码即自动部署**

一个前后端分离的业务系统：

- **后端**：Spring Boot 3.5.16（Java 21）+ MyBatis + Lombok + Hutool
- **前端**：原生 HTML / JS / CSS + Nginx 反向代理
- **存储**：MySQL 8.0（业务数据）+ 阿里云 OSS（文件）
- **部署**：Docker Compose
- **CI/CD**：GitHub Actions（单文件 pipeline.yml）

---

## 🚀 流水线速览

```
git push origin main
        ↓
┌────────────────────────────────────────────────────────────────────┐
│  GitHub Actions（pipeline.yml）                                    │
│                                                                    │
│  ① 编译 & 单元测试    ──→   ② 构建 & 推送镜像到 GHCR              │
│   (build-test)              (build-push-image)                    │
│       ↓                          ↓                                │
│                              ③ SSH 部署到服务器  (deploy)          │
│                                    ↓                               │
│                              服务器 deploy.sh                      │
│                              拉镜像 → 重启 → 健康检查 → 失败回滚  │
└────────────────────────────────────────────────────────────────────┘
```

**开发者每天做的事**：写代码 → `git push` → 等 3 分钟 → 服务已更新。

---

## 目录结构

```
qk-management/
├── backend/                    # Spring Boot 多模块项目
│   └── qk-parent/
│       ├── qk-common/         # 公共工具类
│       ├── qk-entity/         # 实体类
│       └── qk-management/     # 启动模块（main 在此）
├── frontend/                   # 静态前端 + Nginx
│   ├── conf/                  # nginx 配置
│   └── html/                  # 静态资源
├── sql/
│   ├── 01_schema.sql          # 建表 DDL（自动注入）
│   ├── 02_seed.sql            # 脱敏测试数据（自动注入）
│   └── README.md              # 数据库详细说明
├── .github/
│   └── workflows/
│       └── pipeline.yml        # ⭐ 唯一 CI/CD 配置文件（流水线）
├── scripts/
│   └── deploy.sh              # 服务器侧部署脚本（由流水线调用）
├── .env.example               # 密钥字段模板（提交到 git）
├── .gitignore                 # 屏蔽 .env 等敏感文件
├── docker-compose.yml         # MySQL 容器编排
├── Dockerfile.backend         # 后端镜像构建
└── Dockerfile.frontend        # 前端镜像构建
```

---

## 一、系统要求

| 项 | 最低 | 推荐 |
|---|------|------|
| OS | Ubuntu 22.04 LTS | Ubuntu 22.04 / 24.04 LTS |
| CPU | 1 vCPU | 2 vCPU 及以上 |
| 内存 | 2 GB | 4 GB 及以上 |
| 磁盘 | 10 GB | 20 GB+（MySQL 数据会增长） |
| Docker | 24.0+ | 最新稳定版 |
| 网络 | 公网 IP（让用户访问前端） | - |

> 端口规划：前端 `90`、后端 `8080`（建议仅内网）、MySQL `3306`（**仅本机**）。

---

## 二、首次部署（一次性，约 15 分钟）

### 2.1 服务器环境准备（Ubuntu）

```bash
# 装 Docker
sudo apt update
sudo apt install -y ca-certificates curl gnupg lsb-release
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | \
    sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
  https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# 让当前用户免 sudo
sudo usermod -aG docker $USER
newgrp docker
docker --version
```

### 2.2 创建部署目录并克隆项目

```bash
sudo mkdir -p /opt/qk-management
sudo chown -R $USER:$USER /opt/qk-management
cd /opt/qk-management
git clone <your-repo-url> .
chmod +x scripts/deploy.sh
```

> 服务器这次 `git clone` 是为了拿 `scripts/deploy.sh`。之后服务器**不再需要 git 操作** —— 流水线会从 GHCR 拉镜像。

### 2.3 配置服务器密钥 `.env`

> ⚠️ **`.env` 永不提交到 GitHub**。本项目已通过 `.gitignore` / `.dockerignore` 屏蔽。

```bash
cp .env.example .env
chmod 600 .env
vi .env
```

必填字段（其余可保持默认）：

```bash
# GitHub 用户名（小写，不带 @），对应镜像 owner
GHCR_OWNER=your-github-username

# MySQL 容器内访问用 "mysql"；外部 MySQL 改成 IP
MYSQL_HOST=mysql
MYSQL_USER=qk
MYSQL_PASSWORD=YourStrong@Passw0rd    # ← 必改（≥12 位，含大小写+数字+符号）
MYSQL_ROOT_PASSWORD=YourRoot@Passw0rd # ← 必改

OSS_ENDPOINT=oss-cn-beijing.aliyuncs.com
OSS_BUCKET_NAME=your-bucket
OSS_ACCESS_KEY_ID=...
OSS_ACCESS_KEY_SECRET=...
```

> MySQL 默认密码 `123456` 必须修改！

### 2.4 启动 MySQL 容器

```bash
cd /opt/qk-management
docker compose up -d mysql
docker compose logs -f mysql   # 看到 "ready for connections" 后 Ctrl+C
```

MySQL 容器**首次启动**时会自动按字母序执行 `./sql` 下的所有 `.sql` 文件：
- `01_schema.sql` — 建表
- `02_seed.sql`   — 填测试数据

校验：

```bash
docker exec qk-mysql mysql -u qk -p"$MYSQL_PASSWORD" qk -e "SHOW TABLES;"
```

### 2.5 配置 GitHub Secrets（流水线用）

仓库 → **Settings → Secrets and variables → Actions → New repository secret**，添加：

| Secret 名称 | 值 | 用途 |
|------------|----|----|
| `DEPLOY_HOST_PROD` | `your-prod-server.com` | 生产服务器地址 |
| `DEPLOY_HOST_DEV` | `your-dev-server.com` | 开发服务器地址（可与 prod 同） |
| `DEPLOY_USER` | `ubuntu` | SSH 用户名 |
| `DEPLOY_SSH_KEY` | `-----BEGIN OPENSSH PRIVATE KEY-----...` | 私钥全文 |
| `DEPLOY_PORT` | `22` | SSH 端口（可选，默认 22） |

> 这些 Secret 只用于"流水线 SSH 到服务器"，**没有任何业务密钥**（MySQL/OSS 密钥都在服务器 `.env`）。

### 2.6 设置 GHCR 包可见性

> 推荐设为 **Public**，这样服务器可匿名 `docker pull`，无需配置 PAT。

仓库主页 → 顶部 **Packages** → 点击 `qk-backend` → **Package settings** → **Change visibility** → **Public**。对 `qk-frontend` 同样操作。

如果坚持 Private（不推荐），需在服务器 `.env` 添加：

```bash
GHCR_TOKEN=ghp_xxxxxxxxxxxx   # PAT，需要 read:packages 权限
```

### 2.7 触发首次部署

回到本地仓库：

```bash
git checkout main
git push origin main        # 触发流水线全流程
```

打开 GitHub → 仓库 → **Actions** → `Pipeline` → 实时查看三个 Job 进度：
1. **① 编译 & 单元测试**
2. **② 构建 & 推送镜像**
3. **③ 部署到服务器**

### 2.8 验证

```bash
# 在服务器上
docker compose ps         # 3 个 Up 状态
curl -I http://127.0.0.1:90/   # 前端
curl -I http://127.0.0.1:8080/actuator/health   # 后端
```

浏览器访问 `http://<服务器IP>:90/`，看到登录页即首次部署完成。默认账号 `admin` / `123456`（**请立刻改密码**）。

---

## 三、日常使用（开发者视角）

### 3.1 推送代码自动部署

```bash
git add .
git commit -m "feat: 新增导出功能"
git push origin main
# → 等待 3 分钟 → 服务已自动更新
```

**不再需要 SSH 到服务器。** 流水线自动完成：编译 → 测试 → 构建镜像 → 推 GHCR → 拉镜像 → 重启 → 失败回滚。

### 3.2 PR 流程

```bash
git checkout -b feature/export
# ... 开发 ...
git push -u origin feature/export
# → 在 GitHub 上开 PR 到 main
# → 流水线只跑 ① 编译测试，不部署不推镜像
# → 合并后自动部署
```

### 3.3 正式发版（Tag）

```bash
git tag -a v1.0.0 -m "Release 1.0.0"
git push origin v1.0.0
# → 流水线跑全流程，镜像 tag 为 v1.0.0，自动部署到生产
```

### 3.4 手动触发（指定 ref）

GitHub → Actions → Pipeline → **Run workflow** → 可填 ref（如 `v1.0.0`、`dev-abc1234`）。

### 3.5 紧急回滚

```bash
# 方式 1：服务器侧手动回滚到上一个 tag
ssh user@server
cd /opt/qk-management
bash scripts/deploy.sh --rollback

# 方式 2：重新触发流水线，指定上一个 tag
#   GitHub → Actions → Pipeline → Run workflow → ref=v0.9.5
```

---

## 四、数据库

### 4.1 文件结构

```
sql/
├── 01_schema.sql   # 建表 DDL（7 张表，幂等重建）
└── 02_seed.sql     # 脱敏测试数据（4 个账号，密码 123456）
```

详见 [sql/README.md](sql/README.md)。

### 4.2 自动初始化

MySQL 容器首次启动时按字母序执行 `./sql/*.sql`。**仅首次执行**，后续重启/升级不重复。

重置数据库（⚠️ 清空数据）：

```bash
docker compose down mysql
docker volume rm qk-management_mysql_data
docker compose up -d mysql
```

### 4.3 默认测试账号

| 用户名 | 角色 | 密码 |
|--------|------|------|
| `admin`         | 系统管理员 | `123456` |
| `clue_user`     | 线索专员   | `123456` |
| `business_user` | 商机专员   | `123456` |
| `viewer`        | 普通员工   | `123456` |

> ⚠️ 生产环境第一时间改密码或删除 `02_seed.sql`！

### 4.4 添加迁移

生产数据库已上线后，新增字段用 Flyway/Liquibase 或新建带日期前缀的 `.sql`：

```sql
-- sql/migrations/2026-09-12_add_user_avatar.sql
ALTER TABLE user ADD COLUMN avatar VARCHAR(255);
```

手动执行：

```bash
docker exec -i qk-mysql mysql -u root -p"$MYSQL_ROOT_PASSWORD" qk \
  < sql/migrations/2026-09-12_add_user_avatar.sql
```

> 不要直接改 `01_schema.sql`，下次重置数据库会丢失变更。

---

## 五、运维命令

### 5.1 服务管理

```bash
cd /opt/qk-management

docker compose ps               # 查看容器
docker compose logs -f backend  # 实时日志（最近 200 行）
docker compose restart backend  # 重启单个服务
docker compose restart          # 重启全部
docker compose stop             # 停止
docker compose up -d            # 启动（deploy.sh 也用这个）
```

### 5.2 进入容器调试

```bash
docker exec -it qk-backend sh
docker exec -it qk-mysql bash
```

### 5.3 查看镜像

```bash
docker images | grep qk-    # 看本地有哪些 tag
```

### 5.4 数据备份

```bash
docker exec qk-mysql mysqldump -u root -p"$MYSQL_ROOT_PASSWORD" \
  --single-transaction --routines --triggers qk \
  | gzip > /opt/qk-management/backups/qk-$(date +%Y%m%d).sql.gz
```

恢复：

```bash
gunzip -c /opt/qk-management/backups/qk-20260912.sql.gz | \
  docker exec -i qk-mysql mysql -u root -p"$MYSQL_ROOT_PASSWORD" qk
```

### 5.5 自动备份（crontab）

```bash
# 加入 /etc/crontab，每天 3 点备份，保留 30 天
cat | sudo tee /etc/cron.d/qk-backup <<'EOF'
0 3 * * * root cd /opt/qk-management && \
  docker exec qk-mysql mysqldump -u root -p"$MYSQL_ROOT_PASSWORD" \
  --single-transaction qk | gzip > /opt/qk-management/backups/qk-$(date +\%Y\%m\%d-\%H\%M\%S).sql.gz && \
  find /opt/qk-management/backups -name "*.sql.gz" -mtime +30 -delete
EOF
```

---

## 六、Git 工作流

### 6.1 分支策略

```
main              ← 生产分支，受保护，PR 合入，push 自动部署
dev               ← 开发分支，push 自动部署到 dev 环境
feature/*         ← 功能分支（例：feature/user-import）
fix/*             ← 缺陷修复
hotfix/*          ← 紧急修复（从 main 拉出，修完直接合回 main）
```

### 6.2 Commit 规范

采用 [Conventional Commits](https://www.conventionalcommits.org/)：

```bash
git commit -m "feat(user): 批量导入用户，支持 CSV"
git commit -m "fix(login): 修复记住密码失效的问题"
git commit -m "docs: 补充 README 中的 Git 使用规范"
git commit -m "chore: 升级 Spring Boot 到 3.5.16"
```

| type | 用途 |
|------|------|
| `feat` | 新功能 |
| `fix` | 修复 bug |
| `docs` | 仅文档 |
| `refactor` | 重构 |
| `perf` | 性能优化 |
| `chore` | 构建/工具/杂项 |

### 6.3 .gitignore 关键屏蔽规则

| 模式 | 原因 |
|------|------|
| `.env` / `.env.*` | 服务器密钥（MySQL、OSS） |
| `**/src/**/resources/application-local.yaml` | 本地开发个人配置 |
| `**/src/**/resources/application-prod.yaml` | 生产配置 |
| `**/target/` | Maven 产物 |
| `.idea/` / `*.iml` | IDEA 配置 |

提交前必做：

```bash
git status   # 检查没有 .env / application-local.yaml
```

### 6.4 密钥被误提交怎么办？

> ⚠️ **立刻行动——密钥泄露窗口期越短越好**。

```bash
# 1) 立刻吊销所有相关密钥
#    - GitHub PAT:    https://github.com/settings/tokens → Revoke
#    - 阿里云 RAM:    控制台 → 删除 AccessKey
#    - MySQL:         ALTER USER 'qk'@'%' IDENTIFIED BY '新密码';

# 2) 从 git 历史彻底删除
pip install git-filter-repo
git filter-repo --invert-paths --path .env
git filter-repo --invert-paths --path-glob '**/application-local.yaml'

# 3) 强制推送
git push --force --all
git push --force --tags

# 4) 通知所有协作者重新 clone
```

---

## 七、本地开发（IDEA）

### 7.1 导入项目

```bash
git clone <your-repo-url> ~/IdeaProjects/qk-management
# IntelliJ IDEA → Open → 选 ~/IdeaProjects/qk-management
# IDEA 会自动识别为 Maven 多模块项目
```

### 7.2 直接 Run（零配置）

`application.yaml` 已用 `${PLACEHOLDER:默认值}` 语法提供开发默认值（`localhost` / `root` / `123456`），**无需任何环境变量、无需 `.env`、无需 Docker**。

直接 Run `qk-management` 模块的 `Application.java` 即可启动。

### 7.3 覆盖配置（可选）

如需使用真实数据库连接：

```bash
cd backend/qk-parent/qk-management/src/main/resources
cp application-local.yaml.example application-local.yaml
vi application-local.yaml    # 填自己的连接信息（已被 .gitignore 屏蔽）
```

或在 IDEA **Run Configuration → Environment variables** 里加：

```
SPRING_DATASOURCE_URL=jdbc:mysql://your-host:3306/qk
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=xxx
OSS_ACCESS_KEY_ID=...
OSS_ACCESS_KEY_SECRET=...
```

---

## 八、故障排查

### 8.1 流水线失败：编译报错

进入 Actions → Pipeline → ① 编译 单元测试 → 看 Maven 日志。本地复现：

```bash
cd backend/qk-parent
mvn -B -ntp -DskipTests clean compile
```

### 8.2 流水线失败：部署 SSH 连不上

检查 GitHub Secrets：

- `DEPLOY_HOST_PROD` / `DEPLOY_USER` / `DEPLOY_SSH_KEY` 是否填写
- 服务器 `~/.ssh/authorized_keys` 是否包含对应公钥
- 服务器 SSH 端口是否正确（默认 22）
- 服务器防火墙是否放行 22 端口

### 8.3 服务器健康检查超时

```bash
ssh user@server
cd /opt/qk-management
docker compose ps            # 看哪个容器异常
docker compose logs backend  # 后端日志
```

常见原因：MySQL 未启动、`.env` 密码错误、镜像 pull 失败（GHCR 包非 public）。

### 8.4 镜像 pull 失败（GHCR private）

如果 GHCR 包设为 private 但 `.env` 没配 `GHCR_TOKEN`，服务器会拉取失败。两种解决：
- **推荐**：把 GHCR 包设为 public（见 §2.6）
- 或在服务器 `.env` 配 `GHCR_TOKEN=<PAT>`

### 8.5 MySQL 容器反复重启

```bash
docker compose logs mysql | tail -50
# 常见：权限错乱、磁盘满、root 密码未改
# 解决：见 §4.2 重置数据库
```

### 8.6 服务器磁盘满

```bash
docker system df
docker image prune -af       # 清无用镜像
docker volume prune          # ⚠️ 先确认无重要数据
```

---

## 九、安全建议

- [ ] 修改 `.env` 中的所有默认密码（MySQL、OSS）
- [ ] MySQL 端口**不对外**，仅 backend 容器可访问
- [ ] 启用服务器防火墙（UFW / iptables / 安全组）
- [ ] SSH 改用密钥登录，禁用 root + 密码
- [ ] `.env` 文件 `chmod 600`
- [ ] 阿里云 RAM AccessKey 仅授予 OSS 读写权限
- [ ] 定期 `apt upgrade` 更新系统

---

## 十、流水线参考

### 触发规则

| 事件 | 行为 |
|------|------|
| PR → main / dev | 仅 ① 编译测试 |
| push → main | 全流程（→ 生产服务器） |
| push → dev | 全流程（→ 开发服务器） |
| push tag → `v*.*.*` | 全流程（→ 生产服务器） |
| 手动触发 | 全流程，可选指定 ref |

### 镜像标签

| 触发场景 | 镜像 tag | 额外 tag |
|---------|---------|---------|
| push main | `main-<sha>` | `latest`、`<sha>-<run#>` |
| push dev | `dev-<sha>` | `<sha>-<run#>` |
| push tag v1.0.0 | `v1.0.0` | `latest`、`<sha>-<run#>` |

### 关键文件

| 文件 | 职责 |
|-----|-----|
| `.github/workflows/pipeline.yml` | **唯一** CI/CD 配置文件 |
| `scripts/deploy.sh` | 服务器侧部署脚本（流水线 SSH 调用） |
| `.env.example` | 服务器密钥字段模板（提交到 git） |
| `Dockerfile.backend` / `Dockerfile.frontend` | 镜像构建 |

### 服务器侧命令

```bash
# 查看当前部署的 tag
cat /opt/qk-management/.current_tag

# 查看镜像拉取历史
docker images | grep qk-

# 手动重新部署当前 tag
cd /opt/qk-management && bash scripts/deploy.sh

# 回滚到上一个 tag
cd /opt/qk-management && bash scripts/deploy.sh --rollback
```

---

## License

仅供学习与内部使用。