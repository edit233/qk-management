#!/usr/bin/env bash
# ============================================================================
# qk-management 服务器一键初始化脚本
# ----------------------------------------------------------------------------
# 用法（任选其一）：
#   方式 A（推荐）：把这个文件传到服务器，然后 bash server-init.sh
#   方式 B：服务器上直接 curl 运行：
#     curl -fsSL https://raw.githubusercontent.com/<owner>/qk-management/main/scripts/server-init.sh | bash
#
# 作用：
#   1. 检测/安装 Docker
#   2. 创建部署目录 /opt/qk-management
#   3. 克隆项目
#   4. 交互式创建 .env
#   5. 启动 MySQL（等待 ready）
#   6. 校验数据库表已建好
#
# 不会做的事（需要你手动）：
#   - 修改 sshd_config / 添加 authorized_keys（你 SSH 上来时已经搞定）
#   - 真实 .env 密钥的填写（这里只生成模板）
# ============================================================================
set -euo pipefail

# ---------- 颜色 ----------
GREEN='\033[1;32m'; YELLOW='\033[1;33m'; RED='\033[1;31m'; NC='\033[0m'
log()   { printf "${GREEN}[init]${NC} %s\n" "$*"; }
warn()  { printf "${YELLOW}[init]${NC} %s\n" "$*" >&2; }
err()   { printf "${RED}[init]${NC} %s\n" "$*" >&2; }

# ---------- 必须以普通用户运行，不能 root ----------
if [ "$EUID" -eq 0 ]; then
  err "请用普通用户运行：bash server-init.sh   （不要加 sudo）"
  err "脚本内部需要 sudo 安装 docker，会自动提权"
  exit 1
fi

# ---------- 步骤 1：装 Docker ----------
log "步骤 1/6：检查/安装 Docker"
if ! command -v docker &> /dev/null; then
  warn "未检测到 docker，开始安装..."
  curl -fsSL https://get.docker.com | sh
  sudo usermod -aG docker "$USER"
  # 让当前 shell 立即生效（避免重新登录）
  exec sg docker -c "bash $0 $*"
fi

if ! docker compose version &> /dev/null; then
  err "已安装 docker 但缺少 compose 插件"
  err "请手动：sudo apt install -y docker-compose-plugin"
  exit 1
fi
log "  ✓ docker: $(docker --version)"
log "  ✓ compose: $(docker compose version --short)"

# ---------- 步骤 2：创建部署目录 ----------
log "步骤 2/6：创建 /opt/qk-management"
sudo mkdir -p /opt/qk-management
sudo chown -R "$USER:$USER" /opt/qk-management

# ---------- 步骤 3：克隆项目 ----------
cd /opt/qk-management
if [ ! -d .git ]; then
  log "步骤 3/6：克隆项目"
  if [ -z "${REPO_URL:-}" ]; then
    read -rp "  请输入 GitHub 仓库 URL（如 https://github.com/foo/qk-management.git）：" REPO_URL
  fi
  git clone "$REPO_URL" .
else
  log "步骤 3/6：项目已存在，跳过克隆"
fi

# ---------- 步骤 4：生成 .env ----------
log "步骤 4/6：生成 .env"
if [ -f .env ]; then
  warn ".env 已存在，跳过（不会覆盖）"
else
  cp .env.example .env
  chmod 600 .env
  warn ".env 已生成在 /opt/qk-management/.env，请用 vi 编辑后按回车继续："
  warn "  必改字段：MYSQL_PASSWORD / MYSQL_ROOT_PASSWORD / OSS_*"
  read -rp "  编辑完成按回车继续..."
fi

# ---------- 步骤 5：起 MySQL ----------
log "步骤 5/6：启动 MySQL（首次启动会自动执行 sql/01_schema.sql + 02_seed.sql）"
docker compose up -d mysql

# ---------- 步骤 6：等待 MySQL ready ----------
log "步骤 6/6：等待 MySQL 就绪（最多 120 秒）"
ready=false
for i in $(seq 1 60); do
  if docker exec qk-mysql mysqladmin ping -uroot -p"${MYSQL_ROOT_PASSWORD:-123456}" --silent &>/dev/null \
     || docker exec qk-mysql mysql -uroot -p"123456" -e "SELECT 1" &>/dev/null; then
    ready=true; break
  fi
  sleep 2
done

if [ "$ready" = "true" ]; then
  log "  ✓ MySQL 已就绪"
else
  warn "MySQL 似乎未就绪，查看日志："
  docker compose logs --tail=50 mysql
  warn "可继续手动排查（脚本不退出）"
fi

# ---------- 完成 ----------
log ""
log "🎉 初始化完成！"
log ""
log "下一步："
log "  1) 检查数据库表："
log "     docker exec qk-mysql mysql -u qk -p\"\$(grep MYSQL_PASSWORD .env | cut -d= -f2)\" qk -e 'SHOW TABLES;'"
log ""
log "  2) 在 GitHub 仓库 Settings → Secrets 配置 5 个 DEPLOY_* 变量"
log ""
log "  3) 推送代码到 main 分支即可触发自动部署："
log "     git push origin main"
log ""