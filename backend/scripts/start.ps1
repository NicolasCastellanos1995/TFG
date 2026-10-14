$ErrorActionPreference = 'Stop'
Set-Location (Join-Path $PSScriptRoot '..')
Get-Content .env | ForEach-Object { if ($_ -match '^([A-Za-z_][A-Za-z0-9_]*)=(.*)$') { [Environment]::SetEnvironmentVariable($matches[1], $matches[2].Trim('"'), 'Process') } }
$target = $env:DB_TARGET.Trim().ToLowerInvariant()
if ($target -eq 'docker') {
  $name = if ($env:DB_NAME) { $env:DB_NAME } else { 'tfg_db' }
  $env:SELECTED_DB_URL = "jdbc:postgresql://postgres:5432/$name"
  $env:SELECTED_DB_USER = $env:DB_USER; $env:SELECTED_DB_PASSWORD = $env:DB_PASSWORD
  docker compose --profile docker-db up -d postgres
} elseif ($target -eq 'neon') {
  $env:SELECTED_DB_URL = $env:NEON_DB_JDBC_URL; $env:SELECTED_DB_USER = $env:NEON_DB_USERNAME; $env:SELECTED_DB_PASSWORD = $env:NEON_DB_PASSWORD
} else { throw 'DB_TARGET debe ser docker o neon' }
docker compose up -d --build backend
