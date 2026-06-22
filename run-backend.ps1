# Script para ejecutar el Backend de Spring Boot (Descarga Maven de ser necesario)
$projectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$mavenDir = Join-Path $projectDir ".maven"
$mavenVersion = "3.9.6"
$mavenZip = Join-Path $projectDir "maven.zip"
$mavenBinDir = Join-Path $mavenDir "apache-maven-$mavenVersion"
$mvnCmd = Join-Path $mavenBinDir "bin\mvn.cmd"

# Verificar si mvn ya esta en el PATH global
if (Get-Command "mvn" -ErrorAction SilentlyContinue) {
    Write-Host ">>> Detectado Maven en el PATH del sistema. Ejecutando backend..." -ForegroundColor Green
    cd "$projectDir\backend-spring"
    mvn spring-boot:run
    exit
}

# De lo contrario, usar la instalacion local/portable en .maven
if (-not (Test-Path $mvnCmd)) {
    Write-Host ">>> Maven no detectado en el sistema. Descargando una version portable..." -ForegroundColor Yellow
    New-Item -ItemType Directory -Force -Path $mavenDir | Out-Null
    
    $url = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
    Write-Host "Descargando de: $url" -ForegroundColor Cyan
    
    # Descargar
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri $url -OutFile $mavenZip
    
    Write-Host "Extrayendo..." -ForegroundColor Cyan
    Expand-Archive -Path $mavenZip -DestinationPath $mavenDir -Force
    
    # Limpieza
    Remove-Item $mavenZip -Force
    Write-Host ">>> Maven instalado de forma portable en $mavenBinDir" -ForegroundColor Green
}

Write-Host ">>> Ejecutando Spring Boot Backend..." -ForegroundColor Green
cd "$projectDir\backend-spring"
& $mvnCmd spring-boot:run
