#!/usr/bin/env bash
# ============================================================================
# qk-management 服务器侧部署脚本（流水线接收器）
# ----------------------------------------------------------------------------
# 设计定位：
#   由 GitHub Actions pipeline.yml 的 ③ deploy job 通过 SSH 调用。
#   接收 1 个参数：镜像 tag（如 main-abc1234、dev-xyz5678、v1.0.0）。
#
# 职责：
#   1. 校验本机 .env（含所有业务密钥，绝不离开服务器）
#   2. 生成 docker-compose.yml（基于 .env 注入密钥）
#   3. docker pull 指定 tag 的镜像
#   4. docker compose up -d 重启服务
#   5. 健康检查 → 失败自动回滚到上一个 tag
#
# 密钥策略：
#   - 本脚本只读服务器本地 .env（MySQL/OSS 密钥）
#   - GHCR 拉取：若包为 public，匿名即可；若包为 private，需在 .env 配 GHCR_TOKEN
#
# 用法：
#   ./deploy.sh <tag>           # 部署指定 tag
#   ./deploy.sh --rollback      # 回滚到上一个 tag
#
# 首次部署前置：
#   1. cd /opt/qk-management
#   2. cp .env.example .env && vi .env
#   3. chmod +x scripts/deploy.sh
# ============================================================================
set -euo pipefail

# ---------- 参数 ----------
TAG="${1:-latest}"
ACTION="deploy"
if [ "$TAG" = "--rollback" ]; then
  ACTION="rollback"
fi

# ---------- 路径 ----------
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DEPLOY_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$DEPLOY_DIR"

# ---------- 加载本地密钥 ----------
if [ ! -f .env ]; then
  echo "❌ 缺少 .env 文件，请先：cp .env.example .env 并填入密钥"
  exit 1
fi

# shellcheck disable=SC1091
set -a; source .env; set +a

# ---------- 必填字段校验（业务密钥） ----------
required_vars=(
  GHCR_OWNER MYSQL_HOST MYSQL_USER MYSQL_PASSWORD
  OSS_ENDPOINT OSS_BUCKET_NAME OSS_ACCESS_KEY_ID OSS_ACCESS_KEY_SECRET
)
for v in "${required_vars[@]}"; do
  if [ -z "${!v:-}" ]; then
    echo "❌ .env 缺少变量: $v"
    exit 1
  fi
done

# ---------- 镜像仓库认证（可选：GHCR_TOKEN 仅在私有包时需要） ----------
if [ -n "${GHCR_TOKEN:-}" ]; then
  echo "🔐 登录 GHCR（私有包模式）"
  echo "$GHCR_TOKEN" | docker login ghcr.io -u "${GHCR_OWNER}" --password-stdin >/dev/null
else
  echo "🌐 GHCR 公开包模式（无需登录）"
fi

# ---------- 变量默认值 ----------
APP_PORT="${APP_PORT:-90}"
BACKEND_PORT="${BACKEND_PORT:-8080}"
BACKEND_IMAGE="ghcr.io/${GHCR_OWNER}/qk-backend"
FRONTEND_IMAGE="ghcr.io/${GHCR_OWNER}/qk-frontend"
TAG_FILE=".current_tag"

# ---------- 工具函数 ----------
log() { printf '\033[1;34m[deploy]\033[0m %s\n' "$*"; }
warn() { printf '\033[1;33m[deploy]\033[0m %s\n' "$*" >&2; }
err()  { printf '\033[1;31m[deploy]\033[0m %s\n' "$*" >&2; }

health_check() {
  log "健康检查（最多 90s）"
  local ok=false
  for _ in $(seq 1 45); do
    if curl -sf "http://127.0.0.1:${APP_PORT}/" >/dev/null 2>&1 \
       || curl -sf "http://127.0.0.1:${BACKEND_PORT}/actuator/health" >/dev/null 2>&1; then
      ok=true; break
    fi
    sleep 2
  done
  [ "$ok" = "true" ]
}

compose_up() {
  local t="$1"
  log "拉取镜像 ${BACKEND_IMAGE}:${t} / ${FRONTEND_IMAGE}:${t}"
  docker pull "${BACKEND_IMAGE}:${t}"
  docker pull "${FRONTEND_IMAGE}:${t}"

  log "生成 docker-compose.yml（密钥仅来自 .env）"
  cat > docker-compose.yml <<EOF
services:
  backend:
    image: ${BACKEND_IMAGE}:${t}
    container_name: qk-backend
    restart: always
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT:-3306}/${MYSQL_DB:-qk}?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
      - SPRING_DATASOURCE_USERNAME=${MYSQL_USER}
      - SPRING_DATASOURCE_PASSWORD=${MYSQL_PASSWORD}
      - OSS_ENDPOINT=${OSS_ENDPOINT}
      - OSS_BUCKET_NAME=${OSS_BUCKET_NAME}
      - OSS_ACCESS_KEY_ID=${OSS_ACCESS_KEY_ID}
      - OSS_ACCESS_KEY_SECRET=${OSS_ACCESS_KEY_SECRET}
    ports:
      - "${BACKEND_PORT}:8080"

  frontend:
    image: ${FRONTEND_IMAGE}:${t}
    container_name: qk-frontend
    restart: always
    ports:
      - "${APP_PORT}:80"
    depends_on:
      - backend
EOF

  log "启动服务"
  docker compose up -d --remove-orphans
}

# ---------- 回滚 ----------
if [ "$ACTION" = "rollback" ]; then
  if [ ! -f "$TAG_FILE" ]; then
    err "找不到 $TAG_FILE，无法回滚"
    exit 1
  fi
  PREV=$(cat "$TAG_FILE")
  warn "回滚到上一个 tag: $PREV"
  compose_up "$PREV"
  health_check || { err "回滚后健康检查仍失败，请人工介入"; exit 1; }
  log "🎉 回滚成功: tag=${PREV}"
  exit 0
fi

# ---------- 正常部署 ----------
log "准备部署：tag=${TAG}"
[ -f "$TAG_FILE" ] && PREV_TAG=$(cat "$TAG_FILE") || PREV_TAG=""
log "上一版本：${PREV_TAG:-无}"

compose_up "$TAG"

if health_check; then
  echo "$TAG" > "$TAG_FILE"
  log "清理 7 天前的旧镜像"
  docker image prune -af --filter "until=168h" >/dev/null || true
  log "🎉 部署完成：tag=${TAG}"
  exit 0
fi

err "健康检查失败，输出最近日志："
docker compose ps || true
docker compose logs --tail=200 || true

if [ -n "$PREV_TAG" ]; then
  warn "自动回滚到 $PREV_TAG"
  compose_up "$PREV_TAG" || true
fi
exit 1