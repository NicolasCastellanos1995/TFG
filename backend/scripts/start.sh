#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
set -a; source ./.env; set +a
target=$(printf '%s' "${DB_TARGET:-docker}" | tr '[:upper:]' '[:lower:]')
case "$target" in
 docker) export SELECTED_DB_URL="jdbc:postgresql://postgres:5432/${DB_NAME:-tfg_db}" SELECTED_DB_USER="$DB_USER" SELECTED_DB_PASSWORD="$DB_PASSWORD"; docker compose --profile docker-db up -d postgres; docker compose up -d --build backend;;
 neon) export SELECTED_DB_URL="$NEON_DB_JDBC_URL" SELECTED_DB_USER="$NEON_DB_USERNAME" SELECTED_DB_PASSWORD="$NEON_DB_PASSWORD"; docker compose up -d --build backend;;
 *) echo 'DB_TARGET debe ser docker o neon' >&2; exit 1;;
esac
