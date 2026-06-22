# Script para ejecutar el Microservicio de Analitica de Python (FastAPI)
$projectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$analyticsDir = Join-Path $projectDir "analytics-python"
$venvDir = Join-Path $analyticsDir "venv"
$pipExe = Join-Path $venvDir "Scripts\pip.exe"
$pythonExe = Join-Path $venvDir "Scripts\python.exe"
$uvicornExe = Join-Path $venvDir "Scripts\uvicorn.exe"

# Verificar si python esta instalado globalmente
if (-not (Get-Command "python" -ErrorAction SilentlyContinue)) {
    Write-Host ">>> ERROR: Python no esta instalado en el sistema o no esta en el PATH." -ForegroundColor Red
    Write-Host "Por favor, instala Python 3.9+ e intentalo de nuevo." -ForegroundColor Yellow
    exit
}

Write-Host ">>> Iniciando configuracion de entorno de Python..." -ForegroundColor Green
cd $analyticsDir

# Crear entorno virtual si no existe
if (-not (Test-Path $venvDir)) {
    Write-Host "Creando entorno virtual (venv)..." -ForegroundColor Yellow
    python -m venv venv
}

# Actualizar pip e instalar dependencias
Write-Host "Instalando dependencias desde requirements.txt..." -ForegroundColor Yellow
& $pipExe install --upgrade pip
& $pipExe install -r requirements.txt

# Ejecutar servidor FastAPI con Uvicorn en el puerto 8000
Write-Host ">>> Iniciando servidor de analiticas FastAPI..." -ForegroundColor Green
& $uvicornExe main:app --host 0.0.0.0 --port 8000 --reload
